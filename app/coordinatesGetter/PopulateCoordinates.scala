package coordinatesGetter

import scala.concurrent.Future
import scala.util.{Try, Success, Failure} 
import models._
import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws._
import globals.ActiveDatabase


case class coordinate(lat: Double, long: Double)



object PopulateCoordinates {

  var GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json"
  
  implicit val locationReads: Reads[coordinate] = (
    (JsPath \ "lat").read[Double] and
    (JsPath \ "long").read[Double]
  )(coordinate.apply _)
  
  def main(args: Array[String]): Unit = {
 
    Location.listCities() match {
      case Success(cities) =>
        iterateCities(cities)
      case Failure(ex) =>
        println(ex)
    }
  
  }
  
  def iterateCities(cities: Seq[String]): Unit = {
 
    cities.map {city =>
      Location.getLocationsByCity(city) match {
        case Success(cityLocations) => 
          iterateLocations(cityLocations, city)
        case Failure(ex) => 
          println(ex)
      }
    }  
  }
  
  def iterateLocations(locations: Seq[SlimLocation], city: String)(implicit db:ActiveDatabase): Unit = {
    var connection = DB.getConnection(db.name)
    locations.map {location =>
      val existence = SQL(
         """
           SELECT id 
           FROM location,coordinate
           WHERE location.id = """ + location.id + """
           AND location.city = coordinate.city
           AND location.address = coordinate.address;
         """
      ).execute()(connection)
      
      if(existence) {
        
        val move = SQL(
            """
              UPDATE location
              SET location.latitude = coordinate.latitude,
              location.longitude = coordinate.longitude
              WHERE location.id = """ + location.id + """
              AND location.address = coordinate.address
              AND location.city = coordinate.city;
            """
        ).execute()(connection)
        
      }else {
           
        val holder : WSRequestHolder = WS.url(GEOCODING_URL)
    
        //An execution context, required for Future.map:
        implicit val context = scala.concurrent.ExecutionContext.Implicits.global
        
        val parameterString = location.address + city;
        
        val futureResult : Future[JsResult[coordinate]] = holder.withQueryString("address" -> parameterString).get().map{
          response => (response.json \ "location").validate[coordinate]
        }
    
    
        val update = SQL(
           """
             UPDATE location SET latitude = """ + futureResult + """, 
             longitude = """ + futureResult + """, 
             WHERE id = """ + location.id + """;
           """
        ).execute()(connection)

        val backup = SQL(
           """
             INSERT INTO coordinate(address, city, latitude, longitude)
             SELECT address, city, latitude, longitude
             FROM location
             WHERE id = """ + location.id + """;
           """
        ).execute()(connection)
    
      }  
    }     
  }  
}