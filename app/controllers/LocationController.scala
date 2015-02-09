package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._

object LocationController extends Controller {
  /**
   * TODO: Document me.
   */
  def findLocation(city: String) = Action {
    val locationlist = models.Location.getLocationsByCity(city) 
    locationlist match{
      case Success(v) => 
        Ok(views.html.locations.findLocation(Seq.empty[Location]))
      case Failure(e) => 
        InternalServerError(e.toString)
    }
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