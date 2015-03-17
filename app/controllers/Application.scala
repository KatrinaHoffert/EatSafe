package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  /**
   * Displays information about how freaking cool we are.
   */
  def about = Action {
    Ok(views.html.general.about())
  }

  /**
   * Sets the language cookie and then redirects back to the user's previous page. Defaults to the
   * home page if the previous page cannot be determined.
   */
  def setLanguage(languageCode: String) = Action { request =>
    val prevPage = request.headers.get("referer").getOrElse("/")
    Redirect(prevPage).withCookies(
      Cookie("lang", languageCode)
    )
  }
}
