package coordinatesGetter

import scala.concurrent.Future

import models._
import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws._
import globals.ActiveDatabase


case class Location(lat: Double, long: Double)



object InitialGetter {

  var GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json"
  
  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
    (JsPath \ "long").read[Double]
  )(Location.apply _)
  
  def makeRequest(locations: Seq[SlimLocation])(implicit db: ActiveDatabase) {
      
    var connection = DB.getConnection(db.name)
    
    locations.map {location =>
      
       val existence = SQL(
         """
           SELECT id 
           FROM location,coordinate
           WHERE location.address = coordinate.address
           AND location.city = coordinate.city;
         """
      ).execute()(connection)
      
      if(existence) {
        
      }else {
           
        val holder : WSRequestHolder = WS.url(GEOCODING_URL)
    
        //An execution context, required for Future.map:
        implicit val context = scala.concurrent.ExecutionContext.Implicits.global

        val futureResult : Future[JsResult[Location]] = holder.withQueryString("address" -> "101 Cumberland Ave, Saskatoon").get().map{
      
          response => (response.json \ "location").validate[Location]
        }
    
    
        DB.withConnection(db.name) { implicit connection =>
          val query = SQL(
             """
               UPDATE location SET latitude = {latitude}, 
               longitude = {longitude}
               WHERE id = {id};
             """
          ).execute()
        }

    
      }
       
       
    }
      
  }
  
  


  
}