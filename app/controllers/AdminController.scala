package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import globals._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._

object AdminController extends DetectLangController {
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    )
    verifying ("Invalid username or password", result => result match {
      case (username, password) => checkPassword(username, password)
    })
  )

  def checkPassword(username: String, password: String) = {
    (username == "admin" && password == "1234")  
  }

  def login = Action { implicit request =>
    Ok(views.html.admin.login())
  }

  def logout = Action {
    Redirect(routes.AdminController.login).withNewSession.flashing(
      "loggedOut" -> ""
    )
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors =>
        Redirect(routes.AdminController.login).flashing(
          "badPassword" -> ""
        ),
      user =>
        Redirect(routes.AdminController.listAllLocations).withSession(Security.username -> user._1)
    )
  }

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
