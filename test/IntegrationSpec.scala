import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import globals.Globals.testDB
import globals.ActiveDatabase

import org.specs2.mock._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification with Mockito {
  
  "Application" should {
    "run in a browser" in new WithBrowser {
      browser.goTo("/")
    }
  }
 
  /* each runs a set of tests implemented below */
  this.generalControllerIntegration()
  this.showLocationIntegration()
  this.findLocationIntegration()
  this.selectCityIntegration()

 
  def generalControllerIntegration() {
    /**
     * Need to be updated as the view pages are developed
     * Checks to make sure that a page is actually displayed when controller is called
     */
    "Controller" should {

      "show display location page when showLocation is called" in new WithApplication {
        val result = controllers.LocationController.showLocation(7)(FakeRequest())
        assert(status(result) == OK)
        val resultString = contentType(result)
        assert(resultString must beSome.which(_ == "text/html"))
      }
      
      "show find location page when findLocation is called" in new WithApplication {
        val result = controllers.LocationController.findLocation("Saskatoon")(FakeRequest())
        assert(status(result) == OK)
        val resultString = contentType(result)
        assert(resultString must beSome.which(_ == "text/html"))
      }
      
      "show find city page when selectCity is called" in new WithApplication {
        val result = controllers.LocationController.selectCity()(FakeRequest())
        assert(status(result) == OK)
        val resultString = contentType(result)
        assert(resultString must beSome.which(_ == "text/html"))
      }
      
    }
  }
  
  def showLocationIntegration() {
    /**
     * Checks that controller sends appropriate action for showLocation
     */
    "showLocation" should {
      "display information for valid id" in new WithApplication {
        val result = controllers.LocationController.showLocation(7)(FakeRequest())
        assert(status(result) == OK)

        val resultString = contentAsString(result);
        assert(contentAsString(result) must contain("Burstall Curling Rink - Kitchen"))
        assert(contentAsString(result) must contain("Maharg Ave"))
        assert(contentAsString(result) must contain("S0N 0H0"))
      } 
  
      "diplay error for invalid id" in new WithApplication {
        val result = controllers.LocationController.showLocation(-1)(FakeRequest())
        assert(status(result) == INTERNAL_SERVER_ERROR)
      }     
    }
  }
  
  def findLocationIntegration() {
    
    /**
     * Checks that controller displays appropriate pages for findLocation
     */
    "findLocation" should {
      "display information for valid city" in new WithApplication {
        val result = controllers.LocationController.findLocation("Saskatoon")(FakeRequest())
        assert(status(result) == OK)
        assert(contentAsString(result) must contain("Saskatoon"))
        assert(contentAsString(result) must contain("7 Eleven"))
      }
      
      /* empty list displayed with an invalid city is entered*/
      "display nothing for ivalid city" in new WithApplication {
        val result = controllers.LocationController.findLocation("#DOESNTEXIST")(FakeRequest())
        assert(status(result) == OK)
      }
    }
  }
  
  def selectCityIntegration(){
    "selectCity" should {
      "display list of cities" in new WithApplication {
        val result = controllers.LocationController.selectCity()(FakeRequest())
        assert(status(result) == OK)
        assert(contentAsString(result) must contain("Saskatoon"))
      }
    }
  }
}
