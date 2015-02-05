package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  /**
   * The index of the site (served when the user goes to the base URL). This will merely redirect
   * the user to a different portion of the site (for now). When we implement the city selection,
   * that will become the new index page.
   */
  def index = Action {
    Redirect(routes.LocationController.findLocation("Saskatoon"))
  }
}