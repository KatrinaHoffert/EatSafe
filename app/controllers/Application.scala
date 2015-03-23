package controllers

import play.api._
import play.api.mvc._

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
}
