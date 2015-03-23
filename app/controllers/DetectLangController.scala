package controllers

import play.api._
import play.api.mvc._
import play.api.i18n.Lang
import play.api.Play.current

trait DetectLangController extends Controller {
  /**
   * Converts a Request to a Lang by using the cookies and HTTP accepts headers to pick a language.
   * Falls back to english if no good language can be found.
   */
  override implicit def request2lang(implicit request: RequestHeader): Lang = {
    request.cookies.get("lang") match {
      // Cookie is the master choice, if it's set
      case Some(cookie) =>
        Lang(cookie.value)
      // Otherwise pick most optimal language from HTTP accepts header
      case None =>
        Lang.preferred(request.acceptLanguages)
    }
  }
}