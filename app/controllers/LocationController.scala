package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import models.Location
import globals.Globals.defaultDB

object LocationController extends Controller {
  /**
   * Displays a means to select the city. This is the current index page.
   */
  def selectCity = Action {
    Location.listCities() match{
      case Success(cities) =>
        Ok(views.html.locations.selectCity(cities))
      case Failure(ex) =>
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  /**
   * Displays a means to select a location when we know the city.
   */
  def findLocation(city: String) = Action {
    Location.getLocationsByCity(city) match{
      case Success(cityLocations) if !cityLocations.isEmpty => 
        Ok(views.html.locations.findLocation(cityLocations))
      case Success(_) =>
        Ok(views.html.errors.emptyCityError(city))
      case Failure(ex) => 
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  /**
   * Displays information about the location with the given ID.
   *
   * @param locationId The ID of the location to show.
   */
  def showLocation(locationId: Int) = Action {
    Location.getLocationById(locationId) match {
      case Success(location) =>
        Ok(views.html.locations.displayLocation(location))
      case Failure(ex) =>
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  
}
