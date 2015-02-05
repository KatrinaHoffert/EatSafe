package models

import scala.util.{Try, Success, Failure}

/**
 * TODO: Document me.
 */
case class Inspection(date: String, inspectionType: String, reinspectionPriority: String,
    violations: Seq[Violation])