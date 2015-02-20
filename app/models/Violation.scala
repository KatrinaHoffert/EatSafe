package models

import scala.util.{Try, Success, Failure}
import anorm._
import play.api.db.DB
import play.api.Play.current
import globals.ActiveDatabase

/**
 * Represents a health and safety inspection violation.
 * 
 * @param id The ID of the violation type. This is mostly for internal use. The database refers
 * to violations by a type, each which have a unique database. In the future, the ID will be used
 * to provide more information about a violation.
 * @param name A short description of the violation.
 * @param description A more detailed description.
 * @param severity Details how severe the violation is (part of the violation type).
 */
case class Violation(id: Int, name: String, description: String, severity: String)

object Violation {
  /**
   * Gets a list of violations belonging to a given inspection.
   *
   * @param inspectionId The ID of the inspection we want the violations for.
   * @param db this is a implicit parameter that is used to specify what database is to be accessed
   * @returns List of violation objects representing the violations for that inspection.
   */
  def getViolations(inspectionId: Int)(implicit db: ActiveDatabase): Try[Seq[Violation]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
           """
             SELECT violation_id, name, description, priority
             FROM violation v, violation_type vt
             WHERE v.inspection_id = {inspectionId} AND v.violation_id = vt.id;
           """
        ).on("inspectionId" -> inspectionId)
        
        query().map (
          row =>
            Violation(row[Int]("violation_id"), row[String]("name"),
                row[String]("description"), row[String]("priority"))
        ).toList
      }
    }
  }
}