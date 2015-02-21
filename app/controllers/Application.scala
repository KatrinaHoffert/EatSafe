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
    Redirect(routes.LocationController.selectCity())
  }

  /**
   * Returns a JavaScript file that creates a routing object with up-to-date values from the routing
   * file. This allows us to perform reverse routing client side.
   *
   * This script is already included in `views.html.general.mainBody`, so you can use it directly
   * in your HTML.
   *
   * Usage:
   *
   * {{{
   * // Eg, to reverse route the controllers.LocationController.showLocation function:
   * jsRoutes.controllers.LocationController.showLocation(123).url // ==> "/view/123"
   * }}}
   */
  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        LocationController.findLocation,
        LocationController.showLocation
      )
    ).as("text/javascript")
  }
}
