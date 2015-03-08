import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import globals.ActiveDatabase

import org.specs2.matcher._
import org.specs2.mock._

import play.api.i18n.Messages


/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("test")
  
  "Application" should {
    "run in a browser" in new WithBrowser {
      browser.goTo("/")
    }
  }
 
  /* each runs a set of tests implemented below */
  this.showLocationIntegration()
  this.findLocationIntegration()
  this.selectCityIntegration()
  
  def showLocationIntegration() {
    /**
     * Checks that controller sends appropriate action for showLocation
     */
    "showLocation" should {
      "display information for valid id" in new WithApplication {
        val result = controllers.LocationController.showLocation(7)(FakeRequest())
        assert(status(result) == OK)

        val resultString = contentAsString(result);

        assert(contentAsString(result) must contain("Rating:"))
        assert(contentAsString(result) must contain("Most recent issues:"))
        assert(contentAsString(result) must contain(Messages("locations.view.pastInspectionsHeader")))
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
      "display error for negative id city" in new WithApplication {
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
