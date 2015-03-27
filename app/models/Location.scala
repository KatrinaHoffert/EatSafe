package models

import scala.util.{Try, Success, Failure} 
import anorm._
import controllers.LocationForm
import play.api.db.DB
import play.api.Play.current
import util.ActiveDatabase
import play.api.cache.Cache

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
 * @param latitude The latitude of the location. (-90 <= _ <= 90)
 * @param longitude The longitude of the location (-180 < _ <= 180)
 */
case class Location(id: Int, name: String, latitude: Option[Double], longitude: Option[Double],
    address: Option[String], postalCode: Option[String], city: Option[String],
    regionalHealthAuthority: String, inspections: Seq[Inspection]) {
  /** Returns true if the location has at least one inspection. */
  def hasInspections: Boolean = inspections.size != 0

  def rating: Rating.Value = {
    if(inspections.isEmpty) {
      Rating.Unknown
    }
    else {
      inspections(0).reinspectionPriority match {
        case "Low" => Rating.Good
        case "Moderate" => Rating.Fair
        case "High" => Rating.Poor
      }
    }
  }
}

object Location {
  /**
   * Gets a single location by its ID.
   *
   * @param locationId The ID of the desired location. Must exist in the database.
   * @return The location object with the desired ID. Will return Failure if no such ID exists in
   * the database.
   */
  def getLocationById(locationId: Int)(implicit db: ActiveDatabase): Try[Location] = {
    Try {
      require(locationId > 0, "Location ID must be greater than 0.")
      
      DB.withConnection(db.name) { implicit connection =>
        val locationQuery = SQL(
          """
          SELECT id, name, address, postcode, city, rha, latitude, longitude
            FROM location LEFT JOIN coordinates ON (id = location_id)
            WHERE id = {locationId};
          """
        ).on("locationId" -> locationId)
        
        val optionalLocation = locationQuery().map(locationRowToLocation(_, false)).toList.headOption
        optionalLocation.getOrElse {
          throw new IllegalArgumentException("There is no location with ID " + locationId)
        }.get
      }
    }
  }

  /**
   * Gets a list of all Locations with coordinates. These are full location objects.
   */
   def getAllLocationsWithCoordinates()(implicit db: ActiveDatabase): Try[Seq[Location]] = {
    Cache.getOrElse[Try[Seq[Location]]]("allLocations") {
      Try {
        DB.withConnection(db.name) { implicit connection =>
          // Note that the coordinate setter can only make both latitude and longitude NULL or neither,
          // so we only have to check one.
          val query = SQL(
            """
            SELECT id, name, address, postcode, city, rha, latitude, longitude
              FROM location INNER JOIN coordinates ON (id = location_id)
              WHERE latitude IS NOT NULL;
            """
          )

          val tryLocations = query().map((locationRowToLocation(_, true))).toList

          // We have a Seq[Try[Location]], convert it to a Seq[Location]
          tryLocations.map(_.get)
        }
      }
    }
  }

  /**
   * Gets a list of locations in a city. Note that the location objects are complete (and thus the
   * inspections and violations of all of them have been resolved). As a result, this is quite
   * inefficient if we only need information about the location and not the inspections.
   *
   * @param cityName The city in question.
   * @param db this is a implicit parameter that is used to specify what database is to be accessed
   * @return A list of location objects representing each object located in the city.
   */
  def getLocationsByCity(cityName: String)(implicit db: ActiveDatabase): Try[Seq[SlimLocation]] = {
    Try {
      require(cityName.nonEmpty, "City name cannot be empty.")
      DB.withConnection(db.name) { implicit connection =>
        val query = if(cityName.toLowerCase != "unknown city") {
          SQL(
           """
             SELECT id, name, address
             FROM location
             WHERE LOWER(city) = LOWER({cityName});
           """
          ).on("cityName" -> cityName)
        }
        else {
          SQL(
             """
               SELECT id, name, address
               FROM location
               WHERE city IS NULL;
             """
          )
        }
        
        query().map {
          row => SlimLocation(row[Int]("id"), row[String]("name"), row[Option[String]]("address"))
        }.toList
      }
    }
  }
  
  /**
   * Gets a list of city names from the locations in the database. Currently takes no parameters.
   *
   * @return A sequence of Strings equal to every unique city in the DB, in alphabetical order. At
   * the bottom will be "Unknown city", for locations without a city set.
   */
  def listCities()(implicit db:ActiveDatabase): Try[Seq[String]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
          """
            SELECT city
              FROM (
                SELECT count(city), city
                FROM location
                GROUP BY city
                ORDER BY count DESC
              ) AS citiesWithNumLocations
              WHERE city IS NOT NULL;
          """
        )
        
        val cityList = query().map(_[String]("city")).toList

        cityList ++ Seq("Unknown city")
      }
    }
  }

  /**
   * Lists all the locations, but with only the fields needed for the AdminLocation object.
   */
  def getAdminLocations()(implicit db:ActiveDatabase): Try[Seq[AdminLocation]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
           """
             SELECT id, name, address, city
              FROM location;
           """
          )
        
        query().map {
          row => AdminLocation(row[Int]("id"), row[String]("name"), row[Option[String]]("address"),
              row[Option[String]]("city"))
        }.toList
      }
    }
  }

  /**
   * Adds a location from the LocationForm.
   */
  def add(location: LocationForm)(implicit db:ActiveDatabase): Try[Unit] = {
    // The nested tries are so that we can separately handle the case of the transaction failing
    // versus failing to even make a transaction.
    Try {
      DB.withTransaction(db.name) { implicit connection =>
        val attempt = Try {
          val locationQuery = SQL(
            """
              INSERT INTO location (name, address, postcode, city, rha)
                VALUES({name}, {address}, {postcode}, {city}, {rha})
                RETURNING id;
            """
          ).on("name" -> location.name, "address" -> location.address, "postcode" -> location.postalCode,
              "city" -> location.city, "rha" -> location.rha)

          val locationId = locationQuery().map(_[Int]("id")).head

          // Insert each location
          for(inspection <- location.inspections) {
            Inspection.add(inspection, locationId)
          }
        }

        // Check if the insertion was successful, and rollback if not
        attempt match {
          case Success(_) =>
            Unit
          case Failure(ex) =>
            connection.rollback()
            throw ex
        }
      }
    }
  }

  /**
   * Adds a location from the LocationForm.
   */
  def edit(location: LocationForm, locationId: Int)(implicit db:ActiveDatabase): Try[Unit] = {
    // The nested tries are so that we can separately handle the case of the transaction failing
    // versus failing to even make a transaction.
    Try {
      DB.withTransaction(db.name) { implicit connection =>
        val attempt = Try {
          val locationQuery = SQL(
            """
              UPDATE location
                SET name = {name}, address = {address}, postcode = {postcode}, city = {city}, rha = {rha}
                WHERE id = {id};
            """
          ).on("name" -> location.name, "address" -> location.address, "postcode" -> location.postalCode,
              "city" -> location.city, "rha" -> location.rha, "id" -> locationId).execute()

          // Delete all previous inspections for this location
          SQL(
            """
              DELETE FROM inspection
                WHERE location_id = {locationId};
            """
          ).on("locationId" -> locationId).execute()

          for(inspection <- location.inspections) {
            Inspection.add(inspection, locationId)
          }
        }

        attempt match {
          case Success(_) =>
            Unit
          case Failure(ex) =>
            connection.rollback()
            throw ex
        }
      }
    }
  }

  /**
   * Deletes a location by ID.
   * @return Success if the deletion succeeded (which includes the trivial caase of there not being
   * a location with that ID) or Failure if the DBMS complained.
   */
  def delete(id: Int)(implicit db:ActiveDatabase): Try[Unit] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        SQL(
          """
            DELETE FROM location
              WHERE id = {id};
          """
        ).on("id" -> id).execute()
      }
    }
  }

  /**
   * Converts a row from the location table into a Location object. This will query other tables
   * that contain data about the location (eg, the inspection table). The Anorm API does not provide
   * any means of static typing for database rows, so you must ensure that the row that you pass in
   * is indeed a row of the location table.
   *
   * Note that the coordinates are not set here and will always be None in the returned Location.
   *
   * @param row A row from the location table.
   * @param connection this is a implicit parameter that is used to share the database connection to improve performance
   * @return A location object created from that row, with the inspections from the database.
   */
  private def locationRowToLocation(row: Row, firstRowOnly: Boolean)(implicit db: ActiveDatabase,
      connection: java.sql.Connection): Try[Location] = {
    for {
      inspections <- if(!firstRowOnly) Inspection.getInspections(row[Int]("id"))
        else Inspection.getFirstInspection(row[Int]("id"))
    } yield Location(row[Int]("id"), row[String]("name"), row[Option[Double]]("latitude"),
        row[Option[Double]]("longitude"), row[Option[String]]("address"), row[Option[String]]("postcode"),
        row[Option[String]]("city"), row[String]("rha"), inspections)
  }
}

/**
 * A slimmed down version of the Location class used to represent the data we need when selecting
 * a location.
 */
case class SlimLocation(id: Int, name: String, address: Option[String])

/**
 * Alternative version of SlimLocation used for the admin interface.
 */
case class AdminLocation(id: Int, name: String, address: Option[String], city: Option[String])