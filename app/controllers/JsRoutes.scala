package controllers

import play.api._
import play.api.mvc._

object JsRoutes extends Controller {
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
        LocationController.showLocation,
        Application.setLanguage,
        SearchController.searchLocations,
        AdminController.deleteLocation
      )
    ).as("text/javascript")
  }
}
