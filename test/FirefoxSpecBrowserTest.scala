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
import org.openqa.selenium.chrome.ChromeDriver

import play.api.i18n.Messages

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class FirefoxSpecBrowserTest extends Specification {
  
    //Firefox drivers are build into Play I think and thus a path doesnt need to be set
    //System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    

    "give error message when trying submit without input" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.$(".topViewError").getText must contain(Messages("locations.selectCity.noInput")))
    }
     
    "give error page when trying to search for invalid place" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("asdfghjkl")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.pageSource must contain(Messages("errors.emptyCityDesc")))
    }
    
    "display choose location page when location is typed in all caps" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is typed in all lowercase" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      val action = new Actions(browser.getDriver)
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.linkText("Saskatoon"))
      action.moveToElement(element)
      action.click
      action.perform
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, tab is pressed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, right is pressed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then tab is pressed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then right arrow is pressed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then enter arrow is pressed and submitted with enter" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ENTER)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      val action = new Actions(browser.getDriver)
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.linkText("Saskatoon"))
      action.moveToElement(element)
      action.click
      action.perform
      val button = browser.getDriver.findElement(By.id("submitButton"))
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, tab is pressed and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.TAB)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, right is pressed and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then tab is pressed and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.TAB)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then right arrow is pressed and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down arrow then enter is pressed and submitted with button" in new WithBrowser(new FirefoxDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ENTER)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
  
}
