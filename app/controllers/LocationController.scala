package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import models.Location

object LocationController extends Controller {
  /**
   * TODO: Document me.
   */
  def findLocation(city: String) = Action {
    val locationlist = Location.getLocationsByCity(city) 
    locationlist match{
      case Success(v) => 
        Ok(views.html.locations.findLocation(Seq.empty[Location]))
      case Failure(e) => 
        InternalServerError(e.toString)
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
        // TODO: Create a prettier error page
        InternalServerError("Encountered an error.\n\nDetails: " + ex.toString)
    }
  }
}