package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.cache.Cache
import scala.util.{Try, Success, Failure}

object Application extends Controller {
  /**
   * Displays information about how freaking cool we are.
   */
  def about = Action {
    Ok(views.html.general.about())
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

  def prepopulateCache = Action {
    // Call poor performing, cached pieces of code here to pre-populate the cache. Can't call too
    // many, though, as the cache filling up will result in older data being removed.
    val success = for {
      _ <- Location.getAllLocationsWithCoordinates
    } yield Unit

    success match {
      case Success(_) =>
        Ok("All caches populated")
      case Failure(ex) =>
        InternalServerError(ex.toString)
    }
  }

  def clearCache = Action {
    // Add cache keys here as they are used
    Cache.remove("allLocations")

    Ok("All caches cleared")
  }
}
