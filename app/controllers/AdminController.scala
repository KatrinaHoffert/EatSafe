package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import globals._
import play.api.Logger

object AdminController extends DetectLangController {
  /**
   * Displays search results.
   */
  def listAllLocations = Action { implicit request =>
    Location.getAdminLocations match {
      case Success(locations) =>
        Ok(views.html.admin.listAllLocations(locations))
      case Failure(ex) => 
        Logger.error("Could not get list of locations", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }
}
