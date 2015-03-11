package coordinatesGetter

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import anorm.SQL
import globals.ActiveDatabase
import play.api.Play.current
import play.api.db.DB
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.ws.WS
import play.api.libs.ws.WSRequestHolder
import play.api.mvc.Controller
import play.api.libs.json.JsResult
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Represent a coordinate, which contains a latitude and longitude
 * @param lat The latitude of this coordinate
 * @param long The longitude of this coordinate
 */
case class Coordinate(lat: Double, long: Double)

/**
 * Represent a http response, which contains a result that includes a coordinate, and a status code
 * @param coordinate The coordinate
 * @param status The status code, include: OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, UNKNOWN_ERROR
 */
case class Response(coordinate: Coordinate, status: String)

/**
 * Represent a location, that doesn't have coordinate in the database, and need to get the coordinate from Google Map API.
 * Use both address and city to find the exact coordinate
 * @param id The unique of this location
 * @param address The address of this location
 * @param city The city this location is in
 */
case class NoCoordinateLocation(id: Int, address: Option[String], city: Option[String])



object PopulateCoordinates{

  /**
   * The Google Map API URL that accept HTTP get request and return a JSON object that contains 
   * the information and coordinate of each search result.
   * Should only use the first result.
   * Use "address + city" as parameter
   * eg. https://maps.googleapis.com/maps/api/geocode/json?address=101+Cumberland+Ave,+Saskatoon
   */
  var GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json"
  
  
  /**
   * Read a coordinate object from JSON
   */
  implicit val coordinateReads: Reads[Coordinate] = (
    (JsPath \ "geometry" \\ "location" \ "lat").read[Double] and
    (JsPath \ "geometry" \\ "location" \ "lng").read[Double]
  )(Coordinate.apply _)
  
  /**
   * Read a response from JSON
   */
  implicit val responseReads: Reads[Response] = (
    (JsPath \ "results")(0).read[Coordinate].orElse(Reads.pure(Coordinate(0,0))) and
    (JsPath \ "status").read[String]
  )(Response.apply _)
    
  
   /**
   * Insert the coordinate, address, and city to "coordinate" table, whenever a new coordinate 
   * is got from Google Map API. 
   * @param connection The database connection 
   */
  def insertCoordinate(address: String, city: String, coordinate: Coordinate)(implicit connection: java.sql.Connection): Boolean = {
    
    SQL(
       """
         INSERT INTO coordinate(address, city, latitude, longitude)
         VALUES({address}, {city}, {latitude}, {longitude})
       """
    ).on("address" -> address, "city" -> city, "latitude" -> coordinate.lat, "longitude" -> coordinate.long).execute()
  }

  /**
   * Get a list of locations that don't have a coordinate in coordinate table
   * @param connection The database connection
   */
  def getNoCoordinateLocations()(implicit connection: java.sql.Connection): Try[Seq[NoCoordinateLocation]] = {

    Try {
      val query = SQL(
         """
           SELECT id, address, city
           FROM location WHERE id NOT IN (SELECT id
           FROM location INNER JOIN coordinate USING (address, city))
           ORDER BY id;
         """
      )
        
      query().map{
        row => NoCoordinateLocation(row[Int]("id"), row[Option[String]]("address"), row[Option[String]]("city"))
      }.toList
    }
  }
  
  /**
   * The main method for getting coordinates from Google Geocoding API
   * @param db The database that connected to 
   */
  def main()(implicit db: ActiveDatabase): Unit = {
    DB.withConnection { implicit connection =>  
      //Get a list of locations that don't have coordinates
      getNoCoordinateLocations() match {
        case Success(locations) => 
          locations.map { location =>
            
            //Call the web service 
            val holder : WSRequestHolder = WS.url(GEOCODING_URL)
        
            //An execution context, required for Future.map:
            implicit val context = scala.concurrent.ExecutionContext.Implicits.global
            
            //get the address and city
            val address = location.address.getOrElse("Unknown")
            val city = location.city.getOrElse("Unknown")
            //If both address and city are not available, should not look up on Google
            //Because it will return the coordinate of "Saskatchewan, Canada"
            if(address!="Unknown" && city!="Unknown"){
            
              //The parameter for the HTTP get request
              val parameterString = address + ", " + city + ", Saskatchewan, Canada";
              
              //Map the response JSON to a coordinate object
              val futureResult : Future[Response] = holder.withQueryString("address" -> parameterString).get().map{
                response => response.json.as[Response]
              }
              
              futureResult.onComplete{
                case Success(coordinateResult) => 
                  val status = coordinateResult.status
                  val coordinate: Coordinate = coordinateResult.coordinate
                  if(status=="OK") {
                    println("id" + location.id + ": " + parameterString + " lat: "+ coordinate.lat + " long:" + coordinate.long)
                    //store the coordinate
                    insertCoordinate(address, city, coordinate)
                  }else {
                    //for debugging
                    println("id" + location.id + ": " + parameterString + "status" + status)
                  }
                  
                case Failure(ex) => println("id" + location.id + ": " + parameterString + " Failed to get coordinate: " + ex)
              }
              Await.ready(futureResult,Duration(1000, "second"))
            }
          }
        case Failure(ex) =>
          println("Failed to get no-coordinates-locations: " + ex)
      }
    }
  }  
}