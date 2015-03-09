package controllers

import play.api._
import play.api.mvc._
import models._
import scala.util.{Try, Success, Failure}
import scala.util.matching._

object MapController extends Controller {

  /**
   * A function that generates a map. Supplied with the location
   * to be displayed, it will clean the address of any superfluous
   * elements and then direct the browser accordingly.
   * @param address The address of the location within the city
   * @param city The city the location is located within
   */
  def showMap(address: String, city: String) = Action {
    // Remove bad parts of addresses. Currently we just know about "c/o", so we'll remove that here.
    val coMatcher = """[Cc][\\/][Oo]""".r // Case insensitive "c/o" and "c\o"
    val cleanAddress = coMatcher replaceAllIn(address, m => "")
    
    Ok(views.html.locations.displayMap(cleanAddress, city))
  }

  def showCityMap(city: String) = Action {
    Ok(views.html.locations.displayCityMap(city))
  }
}
