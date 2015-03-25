package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import globals._
import play.api.Logger

object SearchController extends DetectLangController {
  /**
   * Displays search results.
   */
  def searchLocations(query: String, city: String, lax: Boolean) = Action { implicit request =>
    SearchEngine.queryLocations(query, city, lax) match {
      case Success(cities) =>
        Ok(views.html.locations.searchResults(city, query, cities, lax))
      case Failure(ex) =>
        Logger.error("Search system failed with query \"\"\"" + query + "\"\"\"", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }
}
