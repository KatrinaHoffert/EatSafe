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
import org.openqa.selenium.JavascriptExecutor

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
  
  "select city page" should {
    "show a typeahead for cities" in new WithBrowser {
      browser.goTo("/")
      browser.pageSource must contain(Messages("locations.selectCity.prompt"))
      browser.getDriver.findElement(By.id("municipality"))
    }
    
    "give error message when trying submit without input" in new WithBrowser {
      browser.goTo("http://localhost:" + port)
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys(Keys.ENTER)
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.noInput"))
    }
    
    "give error message when trying to search for invalid place" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("asdfghjkl")
      typeahead.sendKeys(Keys.ENTER)
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.badInput"))
    }
    
    "display choose location page when location is typed in all caps" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      typeahead.sendKeys(Keys.ENTER)
      browser.url must contain("/find/Saskatoon")
    }
    
     "display choose location page when location is typed in all lowercase" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      browser.url must contain("/find/Saskatoon")
    }
    
    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      browser.url must contain("/find/Saskatoon")
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
      browser.url must contain("/find/Saskatoon")
    }
  } 

  "select location page" should {
    "show a typeahead for locations" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      browser.pageSource() must contain(Messages("locations.selectLocation.prompt"))
      browser.getDriver.findElement(By.id("location"))
    }
    
    "display chosen location when valid option is submitted" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click()
      typeahead.sendKeys("2nd Avenue Grill")
      typeahead.sendKeys(Keys.ENTER)
      browser.url() must contain("/view/")
    }
  }
}
