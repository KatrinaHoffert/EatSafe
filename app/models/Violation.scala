package models

import scala.util.{Try, Success, Failure}

/**
 * case class for violation,
 * ID: id of violation, 1-8 are Critical items, 9-16 are general items
 * severity: General Item, Critical Item, Special
 */
case class Violation(id: Int, name: String, description: String, severity: String)