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
      "Saskatoon Health Authority", Seq.empty[Inspection])
    Ok(views.html.locations.displayLocation(dummyLocation))
  }
}