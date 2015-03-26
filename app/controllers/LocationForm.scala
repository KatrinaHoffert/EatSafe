package controllers

import java.util.Date
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

case class LocationForm(
  name: String,
  address: Option[String],
  postalCode: Option[String],
  city: Option[String],
  rha: String,
  inspections: Seq[InspectionForm]
)

case class InspectionForm(
  date: Date,
  inspectionType: String,
  reinspectionPriority: String,
  violations: Option[String] // Actually a comma separate list of violation IDs
) {
  /** Gets the violation IDs as a sequence. */
  def violationIds: Seq[Int] = {
    violations.map { violationString =>
      violationString
        .split(",")
        .map(_.trim.toInt)
        .toList
    }.getOrElse(Seq.empty[Int])
  }
}

object LocationForm {
  val inspectionMapping = mapping(
    "date" -> date,
    "inspectionType" -> nonEmptyText,
    "reinspectionPriority" -> nonEmptyText,
    "violations" -> optional(nonEmptyText)
  )(InspectionForm.apply)(InspectionForm.unapply)

  val locationForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "address" -> optional(nonEmptyText),
      "postalCode" -> optional(nonEmptyText),
      "city" -> optional(nonEmptyText),
      "rha" -> nonEmptyText,
      "inspections" -> seq(inspectionMapping)
    )(LocationForm.apply)(LocationForm.unapply)
  )
}