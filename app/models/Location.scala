package models

import scala.util.{Try, Success, Failure} 
import anorm._
import play.api.db.DB
import play.api.Play.current
import globals.ActiveDatabase

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
    regionalHealthAuthority: String, inspections: Seq[Inspection]) {
  /** Returns true if the location has at least one inspection. */
  def hasInspections: Boolean = inspections.size != 0
}

object Location {
  /**
   * Gets a single location by its ID.
   *
   * @param locationId The ID of the desired location. Must exist in the database.
   * @returns The location object with the desired ID. Will return Failure if no such ID exists in
   * the database.
   */
  def getLocationById(locationId: Int)(implicit db: ActiveDatabase): Try[Location] = {
    val tryLocation = Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
           """
             SELECT id, name, address, postcode, city, rha
             FROM location
             WHERE id = {locationId};
           """    
        ).on("locationId" -> locationId)
        
        val optionalLocation = query().map(locationRowToLocation).toList.headOption
        optionalLocation.getOrElse {
          throw new IllegalArgumentException("There is no location with ID " + locationId)
        }
      }
    }

    // We have a Try[Try[Location]], let's flatten that
    tryLocation.flatten
  }

  /**
   * Gets a list of locations in a city. Note that the location objects are complete (and thus the
   * inspections and violations of all of them have been resolved). As a result, this is quite
   * inefficient if we only need information about the location and not the inspections.
   *
   * @param cityName The city in question.
   * @param db this is a implicit parameter that is used to specify what database is to be accessed
   * @returns A list of location objects representing each object located in the city.
   */
  def getLocationsByCity(cityName: String)(implicit db: ActiveDatabase): Try[Seq[Location]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
           """
             SELECT id, name, address, postcode, city, rha
             FROM location
             WHERE city = {cityName};
           """    
        ).on("cityName" -> cityName)
        
        val tryLocations = query().map(locationRowToLocation).toList

        // We have a Seq[Try[Location]], convert it to a Seq[Location]
        tryLocations.map(_.get)
      }
    }
  }

  /**
   * Converts a row from the location table into a Location object. This will query other tables
   * that contain data about the location (eg, the inspection table). The Anorm API does not provide
   * any means of static typing for database rows, so you must ensure that the row that you pass in
   * is indeed a row of the location table.
   *
   * @param row A row from the location table.
   * @param db this is a implicit parameter that is used to specify what database is to be accessed
   * @returns A location object created from that row, with the inspections from the database.
   */
  private def locationRowToLocation(row: Row)(implicit db: ActiveDatabase): Try[Location] = {
    Inspection.getInspections(row[Int]("id")) match {
      case Success(inspections) =>
        Success(Location(row[Int]("id"), row[String]("name"), row[String]("address"), row[String]("postcode"),
            row[String]("city"), row[String]("rha"), inspections))
      case Failure(ex) => 
        Failure(ex)
    }
  }
}