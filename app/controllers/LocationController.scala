package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import models.Location
import globals._
import play.api.Logger
import play.api.i18n.Lang

object LocationController extends Controller {
  implicit def getLangFromRequest(implicit request: RequestHeader): Lang = {
    request.cookies.get("lang") match {
      case Some(cookie) => Lang(cookie.value)
      case None => Lang("en") // Default
    }
  }

  /**
   * Displays a means to select the city. This is the current index page.
   */
  def selectCity = Action { implicit request =>
    Location.listCities() match {
      case Success(cities) =>
        Ok(views.html.locations.selectCity(cities))
      case Failure(ex) =>
        Logger.error("City selection failed", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  /**
   * Displays a means to select a location when we know the city.
   * @param city Name of the city. If invalid, we'll end up with an empty list of locations and
   * display a custom error page for that.
   */
  def findLocation(city: String) = Action { implicit request =>
    Location.getLocationsByCity(city.toLowerCase) match {
      case Success(cityLocations) if !cityLocations.isEmpty => 
        Ok(views.html.locations.findLocation(cityLocations))
      case Success(_) =>
        Logger.warn("User found empty city ('" + city + "')")
        NotFound(views.html.errors.emptyCityError(city))
      case Failure(ex) => 
        Logger.error("Location selection failed", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  /**
   * Displays information about the location with the given ID.
   * @param locationId The ID of the location to show.
   */
  def showLocation(locationId: Int) = Action { implicit request =>
    Location.getLocationById(locationId) match {
      case Success(location) =>
        Ok(views.html.locations.displayLocation(location))
      case Failure(ex) =>
        Logger.error("Showing location failed", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }
}
