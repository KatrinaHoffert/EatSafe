package models

import scala.util.{Try, Success, Failure}

/**
 * TODO: Document me!
 */
case class Violation (id: Int, name: String, description: String, severity: String)