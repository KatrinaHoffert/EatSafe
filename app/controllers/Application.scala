package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.cache.Cache
import scala.util.{Try, Success, Failure}

object Application extends DetectLangController {
  /**
   * Displays information about how freaking cool we are.
   */
  def about = Action { implicit request =>
    Ok(views.html.general.about())
  }

  /**
   * Sets the language cookie and then redirects back to the user's previous page. Defaults to the
   * home page if the previous page cannot be determined.
   */
  def setLanguage(languageCode: String) = Action { implicit request =>
    val prevPage = request.headers.get("referer").getOrElse("/")
    Redirect(prevPage).withCookies(
      Cookie("lang", languageCode)
    )
  }
  
  /**
   * Displays more information about a violation.
   */
  def violationInfo(locationId: Int, violationId: Int) = Action { implicit request =>
    Ok(views.html.general.violationInfo(locationId, violationId))
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
