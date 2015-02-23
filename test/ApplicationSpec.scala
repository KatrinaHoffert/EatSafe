import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.filter.FilterConstructor._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.htmlunit.HtmlUnitDriver

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  
  /* 
   * Basic check that all views are being rendered in html
   */
  "Application" should {
    
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }
    
   "show the find location page for Saskatoon" in new WithApplication {
       val saskatoon = route(FakeRequest(GET, "/find/Saskatoon")).get
       contentType(saskatoon) must beSome.which(_ == "text/html")
    }
    
    "show the view locations page for a valid id" in new WithApplication {
      val view = route(FakeRequest(GET, "/view/1")).get
      contentType(view) must beSome.which(_ == "text/html")
    }
    
    "show the select city page" in new WithApplication {
      val view = route(FakeRequest(GET, "/")).get
      contentType(view) must beSome.which(_ == "text/html")
    }
  }
  
  "find city page" should {
    "show a typeahead for cities" in new WithBrowser {
      browser.goTo("/")
      browser.pageSource() must contain("Choose a city")
      browser.getDriver.findElement(By.id("municipality"))
    }
    
    /* Not working with updated typeahead
    "display choose a location page when location is submitted" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click()
      typeahead.sendKeys("Saskatoon")
      browser.click("#submitButton")
      browser.url() must contain("/find/Saskatoon")
    } 
    */
  }
  
  "show city page" should {
    "show a typeahead for locations" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      browser.pageSource() must contain("Choose a location")
      browser.getDriver.findElement(By.id("findLocationID"))
    }
    
 /* Not working with updated typeahead
    "display chosen location when valid option is submitted" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click()
      typeahead.sendKeys("2nd Avenue Grill")
      browser.click("#submitButton")
      browser.url() must contain("/view/3059")
    }
 */
  }

}
