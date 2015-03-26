package controllers

import java.util.Date
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import org.apache.commons.lang3.time.DateFormatUtils
import models.Location
import globals._
import play.api.i18n.Messages

/**
 * Represents the data that the user must supply to add or edit a location. It's not a perfect match
 * to the Location object because the user does not use IDs.
 */
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
    verifying (Messages("admin.validation.invalidPostalCode"), fields => {
      fields.postalCode.map(_.matches("""[A-Za-z][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]""")).getOrElse(true)
    })
    verifying (Messages("admin.validation.invalidRha"), fields => {
      validRhas.contains(fields.rha)
    })
    // While we could do this validation at the inspectionMapping level, we won't because Play
    // won't treat the error like a global error and we'll have to do more work in displaying the
    // error to the user.
    verifying (Messages("admin.validation.invalidInspectionType"), fields => {
      fields.inspections.map { inspection =>
        validInspectionTypes.contains(inspection.inspectionType)
      }.foldRight(true)(_ && _)
    })
    verifying (Messages("admin.validation.invalidReinspectionPriority"), fields => {
      fields.inspections.map { inspection =>
        validReinspectionPriorities.contains(inspection.reinspectionPriority)
      }.foldRight(true)(_ && _)
    })
    verifying (Messages("admin.validation.invalidViolationIds"), fields => {
      fields.inspections.map { inspection =>
        // Matches a comma-separated list, eg, "1,2,3" or "12, 123 ,8"
        inspection.violations.map(_.matches("""^(\d+\s*,\s*)*\d+$""")).getOrElse(true)
      }.foldRight(true)(_ && _) &&
      fields.inspections.flatMap { inspection =>
        inspection.violationIds.map { violationId =>
          // There's only 16 IDs numbered 1 through 16. Anything else is invalid.
          violationId >= 1 && violationId <= 16
        }
      }.foldRight(true)(_ && _)
    })
  )

  /**
   * Converts a Location inton a LocationForm for display on the edit page.
   */
  def locationToForm(location: Location): LocationForm = {
    val inspections = location.inspections.map { inspection =>
      val violationString = inspection.violations.map(_.id).mkString(", ")
      val optionalViolations = if(violationString != "") Some(violationString) else None
      InspectionForm(DateFormatUtils.ISO_DATE_FORMAT.parse(inspection.date), inspection.inspectionType,
          inspection.reinspectionPriority, optionalViolations)
    }

    LocationForm(location.name, location.address, location.postalCode, location.city,
        location.regionalHealthAuthority, inspections)
  }
}