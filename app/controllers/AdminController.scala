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
  /**
   * Represents and validates the login form displayed on the login page.
   */
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    )
    verifying ("Invalid username or password", result => result match {
      case (username, password) => checkPassword(username, password)
    })
  )

  /**
   * Verifies the password to the admin page. Currently a dummy value. Will later be expanded to get
   * a list of possible usernames and passwords from a file or something.
   */
  def checkPassword(username: String, password: String) = {
    (username == "admin" && password == "1234")  
  }

  /**
   * Displays the login form.
   */
  def login = Action { implicit request =>
    Ok(views.html.admin.login())
  }

  /**
   * Logs the user out and takes them back to the login form.
   */
  def logout = Action {
    // Note how we're using flash sessions to just pass a flag. So we only actually need to set
    // the name of the key and the body makes no difference. On the login page, we'll check for
    // if a particular flash session has been set and change the page accordingly.
    Redirect(routes.AdminController.login).withNewSession.flashing(
      "loggedOut" -> ""
    )
  }

  /**
   * Receives the form from the login page. Binding the form validates it. If successful, users will
   * be taken to the location list. OTherwise they'll go back to the log in page with an error.
   */
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
  def listAllLocations = withAuth { username => implicit request =>
    Location.getAdminLocations match {
      case Success(locations) =>
        Ok(views.html.admin.listAllLocations(locations))
      case Failure(ex) => 
        Logger.error("Could not get list of locations", ex)
        InternalServerError(views.html.errors.error500(ex))
    }
  }

  /**
   * Displays a form for adding a new location.
   */
  def addLocation = withAuth { username => implicit request =>
    Ok(views.html.admin.addLocation(LocationForm.locationForm))
  }

  /**
   * Actually adds the location that was created in the addLocation form.
   */
  def performAdd = withAuth { username => implicit request =>
    LocationForm.locationForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.addLocation(formWithErrors))
      },
      location => {
        Location.add(location) match {
          case Success(_) =>
            Redirect(routes.AdminController.listAllLocations).flashing(
              "add" -> "success"
            )
          case Failure(ex) =>
            Logger.error("Failed to insert new location", ex)
            Redirect(routes.AdminController.listAllLocations).flashing(
              "add" -> "failure"
            )
        }
      }
    )
  }

  /**
   * Displays a form for editing an existing location.
   */
  def editLocation(locationId: Int) = withAuth { username => implicit request =>
    Location.getLocationById(locationId) match {
      case Success(location) =>
        val filledForm = LocationForm.locationForm.fill(LocationForm.locationToForm(location))
        Ok(views.html.admin.editLocation(filledForm, locationId))
      case Failure(ex) =>
        Redirect(routes.AdminController.listAllLocations).flashing(
          "viewFailure" -> ""
        )
    }
  }

  /**
   * Saves a location with new data (ie, from the edit).
   */
  def performEdit(locationId: Int) = withAuth { username => implicit request =>
    LocationForm.locationForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.editLocation(formWithErrors, locationId))
      },
      location => {
        Location.edit(location, locationId) match {
          case Success(_) =>
            Redirect(routes.AdminController.listAllLocations).flashing(
              "edit" -> "success"
            )
          case Failure(ex) =>
            Logger.error("Failed to insert new location", ex)
            Redirect(routes.AdminController.listAllLocations).flashing(
              "edit" -> "failure"
            )
        }
      }
    )
  }

  /**
   * Deletes a location with the given ID.
   */
  def deleteLocation(id: Int) = withAuth { username => implicit request =>
    Location.delete(id) match {
      case Success(_) =>
        Redirect(routes.AdminController.listAllLocations).flashing(
          "delete" -> "success"
        )
      case Failure(ex) =>
        Logger.error("Failed (?) to delete location with ID " + id, ex)
        Redirect(routes.AdminController.listAllLocations).flashing(
          "delete" -> "failure"
        )
    }
  }
}

/**
 * An action that checks that the user is authenticated. Pretty much copied from
 * <https://www.playframework.com/documentation/2.0.4/ScalaSecurity>.
 */
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