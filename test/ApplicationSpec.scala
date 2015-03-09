import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.filter.FilterConstructor._

import org.openqa.selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox._
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import play.api.i18n.Messages

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
  
  "select city error page" should {
    "return to select city page when option is selected" in new WithBrowser {
      browser.goTo("/find/octavia")
      val link = browser.webDriver.findElement(By.linkText(Messages("errors.emptyCityTryAgain")))
      link.click
      assert(browser.url must contain("/"))
      assert(browser.pageSource must contain(Messages("locations.selectCity.title")))
    }
  }
  
  "select city page" should {
    
    "give error page when an invalid city url is requested" in new WithApplication {
      val error = route(FakeRequest(GET, "/find/octavia")).get
      assert(status(error) must equalTo(OK))
      assert(contentAsString(error) must contain(Messages("errors.emptyCityDesc")))
    }
    
    "give error message when trying submit without input" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.$(".topViewError").getText must contain(Messages("locations.selectCity.noInput")))
    }
     
    "give error page when trying to search for invalid place" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("asdfghjkl")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.pageSource must contain(Messages("errors.emptyCityDesc")))
    }
    
    "display choose location page when location is typed in all caps" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is typed in all lowercase" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with enter" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val action = new Actions(browser.getDriver)
      typeahead.click
      typeahead.sendKeys("Saskato")
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.linkText("Saskatoon"))
      action.moveToElement(element)
      action.click
      action.perform
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
   
  } 

  "select location page" should {
    "display 500 error page for id that is not assigned to a location" in new WithApplication {
      val error = route(FakeRequest(GET, "/view/1000000")).get
      assert(status(error) must equalTo(INTERNAL_SERVER_ERROR))
      assert(contentAsString(error) must contain(Messages("errors.error500Desc")))
    }
    
    "display 500 error page for id less than 0" in new WithApplication {
      val error = route(FakeRequest(GET, "/view/-100")).get
      assert(status(error) must equalTo(INTERNAL_SERVER_ERROR))
      assert(contentAsString(error) must contain(Messages("errors.error500Desc")))      
    }
    
    "display chosen location when valid option is submitted" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click()
      typeahead.sendKeys("2nd Avenue Grill")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
    }
  }
  
  "display location page" should {
    "display show map page when address is clicked" in new WithBrowser {
      browser.goTo("/view/1")
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.tagName("a"))).perform
      action.click.perform
      assert(browser.url must contain("map"))
      assert(browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
    }
    
    "display regional health authority when link is clicked" in new WithBrowser {
      //Find a place in the Saskatoon Health Region
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click()
      typeahead.sendKeys("2nd Avenue Grill")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      
      //Find the link to the health region web page
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.partialLinkText("Health"))).perform
      action.click.perform
      assert(browser.url must contain("saskatoonhealthregion"))
    }
  }
  
  //404 pages have not been implemented yet
  /*
  "404 page" should {
    "be loaded when an invalid url is entered" in new WithApplication {
      val error = route(FakeRequest(GET, "/bubblzzz")).get
      assert(status(error) must equalTo(404)) 
    }
  }
  */
  
  "display map page" should {
    "render a map on the page with a header" in new WithBrowser {
      //Navigate to a page with a map (uses previously tested navigation)
      browser.goTo("/view/1")
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.tagName("a"))).perform
      action.click.perform
      
      assert(browser.url must contain("map"))
      assert(browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
      assert(browser.pageSource must contain("maps.googleapis.com"))
    }
  }
}
