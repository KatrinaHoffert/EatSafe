package controllers

import scala.util.{Try, Success, Failure}
import play.api._
import play.api.mvc._
import models._
import globals._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._

object AdminController extends DetectLangController with Secured {
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
  def listAllLocations = withAuth  { username => implicit request =>
    Location.getAdminLocations match {
      case Success(locations) =>
        Ok(views.html.admin.listAllLocations(locations))
      case Failure(ex) => 
        Logger.error("Could not get list of locations", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }
}

trait Secured {
  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = {
    Results.Redirect(routes.AdminController.login)
  }

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}