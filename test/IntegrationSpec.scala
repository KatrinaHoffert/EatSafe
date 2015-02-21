import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {
  "Application" should {
    "run in a browser" in new WithBrowser {
      browser.goTo("/")
    }
  }
 
  this.generalControllerIntegration()
  this.showLocationIntegration()
  this.findLocationIntegration()

 
  def generalControllerIntegration(){
    /**
     * Need to be updated as the view pages are developed
     * Checks to make sure that a page is actually displayed when controller is called
     */
    "Controller" should {

      "show display location page when showLocation is called" in new WithApplication{
        val result = controllers.LocationController.showLocation(7)(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
      }
      
      "show find location page when findLocation is called" in new WithApplication{
        val result = controllers.LocationController.findLocation("Saskatoon")(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
      }
    }
  }
  
  def showLocationIntegration(){
    /**
     * Dependent on the view actually displaying information
     */
    "showLocation" should {
      "display information for valid id" in new WithApplication{
        val result = controllers.LocationController.showLocation(7)(FakeRequest())
        status(result) must equalTo(OK)
<<<<<<< HEAD
        contentAsString(result) must contain("Burstall Curling Rink - Kitchen")
        contentAsString(result) must contain("Maharg Ave")
        contentAsString(result) must contain("S0N 0H0")
      } 
  
      "diplay error for invalid id" in new WithApplication{
        val result = controllers.LocationController.showLocation(-1)(FakeRequest())
        status(result) must equalTo(INTERNAL_SERVER_ERROR)
      }     
    }
  }
  
  def findLocationIntegration(){
    
    /**
     * Dependent on the view actually showing information
     */
    "findLocation" should {
      "display information for valid city" in new WithApplication{
        val result = controllers.LocationController.findLocation("Saskatoon")(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Saskatoon")
        contentAsString(result) must contain("7 Eleven")
      }
      
      "display nothing for ivalid city" in new WithApplication{
        val result = controllers.LocationController.findLocation("#DOESNTEXIST")(FakeRequest())
        status(result) must equalTo(OK)
      }
    }
  }
  
}
