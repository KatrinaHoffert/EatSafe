package models

import scala.util.{Try, Success, Failure}

/**
 * case class for inspection,
 * inspectionType: includes Routine, Follow-up, Special, etc.
 * reinspectPriority: includes Low, Moderate, High,
 * violations: a list of violations of this inspection
 */
case class Inspection(date: String, inspectionType: String, reinspectionPriority: String,
    violations: Seq[Violation])