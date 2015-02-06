package models

import scala.util.{Try, Success, Failure}

/**
 * case class for each location (restaurant),
 * ID: unique id of the location,
 * inspections: a sequence of inspections of this location
 */
case class Location (id: Int, name: String, address: String, postalCode: String, city: String,
  regionalHealthAuthority: String, inspections: Seq[Inspection])

object Location {
  /**
   * TODO: Document me.
   */
  def getLocationById(id: Int): Try[Location] = {
    // TODO -- returning dummy data right now
    Try(Location(123, "Foo", "123 Fake St", "S1K 2N3", "Saskatoon", "Saskatoon Health Authority",
        Seq.empty[Inspection]))
  }
}