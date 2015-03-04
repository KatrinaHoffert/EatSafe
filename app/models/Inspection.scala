package models

import scala.util.{Try, Success, Failure}
import anorm._
import play.api.db.DB
import play.api.Play.current
import globals.ActiveDatabase


/**
 * Represents a single inspection of a location.
 *
 * @param date The date of the inspection.
 * @param inspectionType
 */
case class Inspection(date: String, inspectionType: String, reinspectionPriority: String,
    violations: Seq[Violation])

object Inspection {
  /**
   * Gets a list of inspections belonging to a given location.
   *
   * @param locationId The ID of the location we want the inspections for.
   * @param connection this is a implicit parameter that is used to share the database connection to improve performance
   * @return List of inspection objects representing the inspections for that location.
   */
  def getInspections(locationId: Int)(implicit connection: java.sql.Connection): Try[Seq[Inspection]] = {
    Try {
      require(locationId > 0, "Location IDs should be greater than 0.")
    
      
        val query = SQL(
           """
             SELECT id, inspection_date, inspection_type, reinspection_priority
             FROM inspection
             WHERE location_id = {locationId}
             ORDER BY inspection_date DESC;
           """    
        ).on("locationId" -> locationId)
        
        // Here, we have a Seq[Try[Inspection]], since getting the violations can fail, which
        // causes the inspection to fail.
        val tryInspections = query().map (
          row =>
            Violation.getViolations(row[Int]("id")).map { violations =>
              Inspection(row[java.util.Date]("inspection_date").toString, row[String]("inspection_type"),
                row[String]("reinspection_priority"), violations)
            }
        ).toList

        // And convert that into a Seq[Inspection] (which the outside Try will wrap into a
        // Try[Seq[Inspection]])
        tryInspections.map(_.get)
    }
  }
}
