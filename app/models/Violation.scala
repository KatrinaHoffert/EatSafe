package models

import scala.util.{Try, Success, Failure}
import anorm._
import play.api.db.DB
import play.api.Play.current
import util.ActiveDatabase

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
   * The flyweight pool. If it's None, it hasn't been generated yet (do so with initPool). Otherwise
   * it's an array that maps IDs (indices are id - 1) to Violations. Never access this directly. Use
   * getViolationById.
   */
  private var violations: Option[Array[Violation]] = None

  /**
   * Attempts to create the flyweight pool. Failure will do nothing, but is detectable since the pool
   * will still be None.
   */
  private def initPool()(implicit db: ActiveDatabase): Unit = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = SQL(
         """
          SELECT id, name, description, priority
          FROM violation_type;
         """
        )
        
        violations = Some(query()
          .map {
            row =>
              Violation(row[Int]("id"), row[String]("name"), row[String]("description"),
                  row[String]("priority"))
          }
          .toList
          .sortBy(_.id)
          .toArray)
      }
    }
  }

  /**
   * Gets a violation by ID. Fetches the violations from a pool, so it's guaranteed that two violations
   * with the same ID refer to the same object.
   *
   * @param id The ID of the violation we want.
   * @return The Violation with that ID, if it exists. Failure otherwise. Can also return Failure
   * for database errors.
   */
  def getViolationById(id: Int)(implicit db: ActiveDatabase): Try[Violation] = {
    violations match {
      case Some(violations) =>
        Try(violations(id - 1))
      case None =>
        initPool()
        violations match {
          case Some(violations) =>
            Try(violations(id - 1))
          case None =>
            Failure(new Exception("Failed to generate violations pool"))
        }
    }
  }

  /**
   * Gets a list of violations belonging to a given inspection.
   *
   * @param inspectionId The ID of the inspection we want the violations for.
   * @param connection this is a implicit parameter that is used to share the database connection to improve performance
   * @return List of violation objects representing the violations for that inspection.
   */
  def getViolations(inspectionId: Int)(implicit db: ActiveDatabase, connection: java.sql.Connection):
      Try[Seq[Violation]] = {
    Try {
      require(inspectionId > 0, "Inspection ID must be greater than 0.")
 
        val query = SQL(
           """
             SELECT violation_id
             FROM violation v, violation_type vt
             WHERE v.inspection_id = {inspectionId} AND v.violation_id = vt.id;
           """
        ).on("inspectionId" -> inspectionId)
        
        val tryViolations = query().map(row => getViolationById(row[Int]("violation_id"))).toList

        // We have a Seq[Try[Location]], convert it to a Seq[Location]
        tryViolations.map(_.get)
    }
  }
}
