package controllers

import play.api._
import play.api.mvc._

object LocationController extends Controller {
  /**
   * TODO: Document me.
   */
  def findLocation(city: String) = Action {
    Ok(views.html.locations.findLocation())
  }

  /**
   * TODO: Document me.
   */
  def showLocation(locationId: Int) = Action {
    Ok(views.html.locations.displayLocation())
  }
}