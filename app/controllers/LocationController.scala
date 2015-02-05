package controllers

import play.api._
import play.api.mvc._
import models._

object LocationController extends Controller {
  /**
   * TODO: Document me.
   */
  def findLocation(city: String) = Action {
    Ok(views.html.locations.findLocation(Seq.empty[Location]))
  }

  /**
   * TODO: Document me.
   */
  def showLocation(locationId: Int) = Action {
    val dummyLocation = Location(123, "Foo", "123 Fake St", "S1K 2N3", "Saskatoon",
      "Saskatoon Health Authority", Seq(
        Inspection("2014-01-01", "Type", "Priority", Seq(
          Violation(23, "Bad thing", "Desc", "asdas"),
          Violation(42, "Different thing", "Dessdfsdfc", "asdas")
        )),
        Inspection("2014-01-01", "Type", "Priority", Seq(
          Violation(42, "Different thing", "Dessdfsdfc", "asdas"),
          Violation(23, "Bad thing", "Desc", "asdas")
        )),
        Inspection("2014-01-01", "Type", "Priority", Seq.empty[Violation])
      )
    )
    Ok(views.html.locations.displayLocation(dummyLocation))
  }
}