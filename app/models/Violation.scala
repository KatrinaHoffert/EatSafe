package models

import scala.util.{Try, Success, Failure}
import anorm._
import play.api.db.DB
import play.api.Play.current

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
  def getViolations(inspectionId: Int): Try[Seq[Violation]] = {
    Try {
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT violation_id, description, priority
             FROM violation v, violation_type vt
             WHERE v.inspection_id = {id} v.violation_id = vt.violation_id;
           """
        ).on("id" -> inspectionId)
        
        query().map (
          row =>
            // TODO: Update once the violation type names are in the database
            Violation(row[Int]("violation_id"), "VIOLATION NAMES NOT IN DB",
                row[String]("description"), row[String]("priority"))
        ).toList
      }
    }
  }
}