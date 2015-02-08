package models

import scala.util.{Try, Success, Failure} 
import anorm._
import play.api.db.DB
import play.api.Play.current


/**
 * Represents a location (which is anywhere that can be audited for health inspections, such as
 * restaurants, cafeterias, and fast food places).
 *
 * @param id A unique ID for the location. This is used to refer to a specific location (eg, in
 * URLs and function arguments).
 * @param name The location name.
 * @param address The street address.
 * @param postalCode A 7 character postal code (3 characters, a space, and 3 more characters).
 * @param city The city the location is in.
 * @param regionalHealthAuthority The RHA that performs inspections for this location.
 * @param inspections A list of inspections that have been done on the location.
 */
case class Location (id: Int, name: String, address: String, postalCode: String, city: String,
  regionalHealthAuthority: String, inspections: Seq[Inspection])

object Location {
  def getLocationById(locationId: Int): Try[Location] = {
    val tryLocation = Try {
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT id, name, address, postcode, city, rha
             FROM location
             WHERE id = {locationId};
           """    
        ).on("locationId" -> locationId)
        
        query().map (
          row =>
            Inspection.getInspections(row[Int]("id")) match {
              case Success(inspections) =>
                Success(Location(row[Int]("id"), row[String]("name"), row[String]("address"), row[String]("postcode"),
                    row[String]("city"), row[String]("rha"), inspections))
              case Failure(ex) => 
                Failure(ex)
            }
        ).toList.head
      }
    }

    // We have a Try[Try[Location]], let's flatten that
    tryLocation.flatten
  }

  def getLocationsByCity(cityName: String): Try[Seq[Location]] = {
    Try {
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT id, name, address, postcode, city, rha
             FROM location
             WHERE city = {cityName};
           """    
        ).on("cityName" -> cityName)
        
        val tryLocations = query().map (
          row =>
            Inspection.getInspections(row[Int]("id")) match {
              case Success(inspections) =>
                Success(Location(row[Int]("id"), row[String]("name"), row[String]("address"), row[String]("postcode"),
                    row[String]("city"), row[String]("rha"), inspections))
              case Failure(ex) => 
                Failure(ex)
            }
        ).toList

        // We have a Seq[Try[Location]], convert it to a Seq[Location]
        tryLocations.map(_.get)
      }
    }
  }
}