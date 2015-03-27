package coordinatesGetter

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import anorm.SQL
import util.ActiveDatabase
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
 * Populates the coordinates table with coordinates for all locations that do not already have
 * coordinates. The coordinates table maps location IDs to their coordinates. Uses Bing Maps to
 * handle the 
 */
object PopulateCoordinates{
  /**
   * Supply your own Bing Maps API key here.
   */
  val BING_MAPS_API_KEY = ""

  /**
   * Bing Maps Geocoding unstructured URL. Takes in query strings for country, city, etc. See
   * <https://msdn.microsoft.com/en-us/library/ff701714.aspx> for documentation.
   */
  var GEOCODING_URL = "http://dev.virtualearth.net/REST/v1/Locations"


  /**
   * Represent a coordinate, which contains a latitude and longitude.
   */
  case class Coordinate(lat: Double, lon: Double)

  /**
   * Read a Coordinate from JSON. Rather messy due to how complicated the output of Bing Maps
   * Geocoding response is.
   */
  implicit val coordinateReads: Reads[Coordinate] = (
    (((JsPath \ "resourceSets")(0) \ "resources")(0) \ "point" \ "coordinates")(0).read[Double] and
    (((JsPath \ "resourceSets")(0) \ "resources")(0) \ "point" \ "coordinates")(1).read[Double]
  )(Coordinate.apply _)
  
  /**
   * Insert the coordinate, address, and city to "coordinate" table.
   */
  def insertCoordinate(locationId: Int, coordinate: Coordinate)(implicit connection: java.sql.Connection) = {
    SQL(
       """
         INSERT INTO coordinates(location_id, latitude, longitude)
         VALUES({locationId}, {latitude}, {longitude})
       """
    ).on("locationId" -> locationId, "latitude" -> coordinate.lat, "longitude" -> coordinate.lon).execute()
  }

  /**
   * Inserts null values for when we failed to get a coordinate.
   */
  def insertNullCoordinate(locationId: Int)(implicit connection: java.sql.Connection) = {
    SQL(
       """
         INSERT INTO coordinates(location_id, latitude, longitude)
         VALUES({locationId}, NULL, NULL)
       """
    ).on("locationId" -> locationId).execute()
  }

  /**
   * Get a list of locations that don't have a coordinate in coordinates table.
   */
  def getNoCoordinateLocations()(implicit connection: java.sql.Connection): Try[Seq[NoCoordinateLocation]] = {
    Try {
      val query = SQL(
         """
           SELECT id, address, city
           FROM location WHERE id NOT IN (
            SELECT id
              FROM location JOIN coordinates ON (id = location_id)
            );
         """
      )

      query().map{ row =>
        NoCoordinateLocation(row[Int]("id"), row[Option[String]]("address"), row[Option[String]]("city"))
      }.toList
    }
  }
  
  def main()(implicit db: ActiveDatabase): Unit = {
    // An execution context, required for Future.map:
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global

    DB.withConnection { implicit connection =>
      // Get a list of locations that don't have coordinates
      getNoCoordinateLocations() match {
        case Success(locations) => 
          locations.map { location =>
            val request = WS.url(GEOCODING_URL)

            // If both address or city are not available, there's nothing meaningful too look up
            if(location.address.isDefined && location.city.isDefined) {
              // Make the request
              val futureResult = request.withQueryString(
                "countryRegion" -> "CA",
                "adminDistrict" -> "SK",
                "locality" -> location.city.getOrElse(""),
                "addressLine" -> location.address.getOrElse(""),
                "key" -> BING_MAPS_API_KEY
              ).get().map { response =>
                response.json.as[Option[Coordinate]]
              }
              
              // Called when the request completes
              futureResult.onComplete{
                case Success(optionalCoordinate) => 
                  if(optionalCoordinate.isDefined) {
                    println(location.id + " (" + location.address.getOrElse("NULL") + ", " +
                        location.city.getOrElse("NULL") + ") -> " + optionalCoordinate.get)
                    insertCoordinate(location.id, optionalCoordinate.get)
                  }
                  else {
                    println(location.id + " (" + location.address.getOrElse("NULL") + ", " +
                        location.city.getOrElse("NULL") + ") -> NULL")
                    insertNullCoordinate(location.id)
                  }
                case Failure(ex) =>
                  println("Failed to get coordinate for location " + location.id + ": " + ex)
              }
              Await.ready(futureResult, Duration(1000, "second"))
            }
          }
        case Failure(ex) =>
          println("Failed to get list of locations that need coordinates: " + ex)
      }
    }
  }  
}

/**
 * Represent a location, that doesn't have coordinate in the database, and need to get the
 * coordinate from Bing Maps API.
 */
case class NoCoordinateLocation(
  id: Int,
  address: Option[String],
  city: Option[String]
)