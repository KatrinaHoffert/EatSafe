package models

/**
 * TODO: Document me.
 */
case class Location (id: Int, name: String, address: String, postalCode: String, city: String,
  regionalHealthAuthority: String, inspections: Seq[Inspection])