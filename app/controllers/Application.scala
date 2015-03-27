package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.cache.Cache
import scala.util.{Try, Success, Failure}
import util.globals._

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
   * Attempts to pre-populate the cache by running code that will cache data (the code that is called
   * must do the actual caching). This way users can immediately access pages that would otherwise
   * require a slow first load.
   */
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

  /**
   * Clears all the caches. Call this when the database changes.
   */
  def clearCache = Action {
    // Can't use Cache.clear() for some reason, since that will choke on incompatible versions of
    // the serialized content. So we must instead do some hacky Java code to get the actual
    // EhCache manager. Really undesirable because this doesn't use the clean, Scala API and is
    // specific to EhCache (not that I expect to change this).
    (new play.api.cache.EhCachePlugin(current)).manager.clearAll()

    Ok("All caches cleared")
  }

  /**
   * The path passed to this is without a trailing slash, but this is called (by the routing system)
   * only when there is a trailing slash. So in other words, this removes that slash.
   */
  def untrail(path: String) = Action { 
    MovedPermanently("/" + path)
  }
}
