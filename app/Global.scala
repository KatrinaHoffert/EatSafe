import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
import play.api.Logger

/**
 * Special file that the Play Framework will use for the error pages. This differs from globals.scala
 * in that it's an actual object and its values are *always* in scope without having to import them.
 * Bceause this property messes with the tests (we don't want the global ActiveDatabase to be
 * used in the tests), this object is used solely for the special hooks described in
 */
object Global extends GlobalSettings with controllers.DetectLangController{
  /**
   * HTTP 500 errors. Ideally, this one should never happen. All our errors are supposed to be
   * manually caught at the controller level, to allow for more specific logging and error handling.
   * If this ever happens, it's likely a bug on our end.
   */
  override def onError(request: RequestHeader, ex: Throwable) = {
    implicit val requestImplicit = request
    Logger.error("User encountered unexpected error. This is probably a bug on our end.", ex)
    Future.successful(InternalServerError(views.html.errors.error500(ex)))
  }

  /**
   * HTTP 404 errors.
   */
  override def onHandlerNotFound(request: RequestHeader) = {
    implicit val requestImplicit = request
    Logger.warn("User hit 404 at " + request.path)
    Future.successful(NotFound(views.html.errors.error404()))
  }

  /**
   * HTTP 400 errors.
   */
  override def onBadRequest(request: RequestHeader, error: String) = {
    implicit val requestImplicit = request
    Logger.warn("User hit 400 at " + request.path + "\nError message is: " + error)
    Future.successful(BadRequest(views.html.errors.error400()))
  }
}