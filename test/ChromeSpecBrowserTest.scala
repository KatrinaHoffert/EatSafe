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
class ChromeSpecBrowserTest extends Specification {
  
    //the chromeDriver needs to know where to look for the driver application currently its in EatSafe/chromedriver.exe
    System.setProperty("webdriver.chrome.driver", "webDrivers/chromedriver.exe");
    
    "All pages should be able to access 'About' Page" in new WithBrowser(new ChromeDriver) {
   //find city
    browser.goTo("/")
    val action = new Actions(browser.getDriver)
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    browser.pageSource must contain (Messages("about.title"))
    
    //find location
    browser.goTo("/find/Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    browser.pageSource must contain (Messages("about.title"))

    //show location page
    browser.goTo("/view/1")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    browser.pageSource must contain (Messages("about.title"))   
    
    //500 error page
    browser.goTo("/view/1000000")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    browser.pageSource must contain (Messages("about.title"))
    
    //find city error page
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    browser.pageSource must contain (Messages("about.title"))
    
    //TODO multimap page
    
  }  
  
  "All pages should have link to 'Creative Commons' Page" in new WithBrowser(new ChromeDriver) {
    //find city
    browser.goTo("/")
    val action = new Actions(browser.getDriver)
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
    
    //find location
    browser.goTo("/find/Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
     

    //show location page
    browser.goTo("/view/1")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
        
    
    //500 error page
    browser.goTo("/view/1000000")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
     
    
    //find city error page
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
     
   //TODO multimap page
  }  
  
  
  
  "All pages should be able to access get back to First Page" in new WithBrowser(new ChromeDriver) {
   //find city
    browser.goTo("/")
    val action = new Actions(browser.getDriver)
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")
    
    //find location
    browser.goTo("/find/Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")

    //show location page
    browser.goTo("/view/1")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")   
    
    //500 error page
    browser.goTo("/view/1000000")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")
    
    //find city error page
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")
    
    //find city error page there are 2 ways to get back from here
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("errors.emptyCityTryAgain")))).perform
    action.click.perform
    browser.url must contain ("/")
    
    //display map page
     browser.goTo("/map?address=610+2nd+Ave+N&city=Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")
    
    //about page
    browser.goTo("/about")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    browser.url must contain ("/")
    //TODO multimap page
     
  }
    
    "give error message when trying submit without input" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.$(".topViewError").getText must contain(Messages("locations.selectCity.noInput")))
    }
     
    "give error page when trying to search for invalid place" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("asdfghjkl")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.pageSource must contain(Messages("errors.emptyCityDesc")))
    }
    
    "display choose location page when location is typed in all caps" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/SASKATOON"))
      browser.pageSource must contain(Messages("locations.selectLocation.title"))//got to the next page, not error page
    }
    
    "display choose location page when location is typed in all lowercase" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/saskatoon"))
      browser.pageSource must contain(Messages("locations.selectLocation.title"))//got to the next page, not error page
    }
    
    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      val action = new Actions(browser.getDriver)
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.className("typeahead-display"))
      action.moveToElement(element)
      action.click
      action.perform
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, tab is pressed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, right is pressed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then tab is pressed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then right arrow is pressed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then enter arrow is pressed and submitted with enter" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ENTER)
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with button" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      val action = new Actions(browser.getDriver)
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.className("typeahead-display"))
      action.moveToElement(element)
      action.click
      action.perform
      val button = browser.getDriver.findElement(By.id("submitButton"))
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, tab is pressed and submitted with button" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.TAB)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, right is pressed and submitted with button" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      button.click
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, down then tab is pressed and submitted with button" in new WithBrowser(new ChromeDriver) {
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
    
    "display choose location page when location is partially typed, down then right arrow is pressed and submitted with button" in new WithBrowser(new ChromeDriver) {
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
    
    "display choose location page when location is partially typed, down arrow then enter is pressed and submitted with button" in new WithBrowser(new ChromeDriver) {
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
    
    "clear text field with clear typeahead button is pressed" in new WithBrowser(new ChromeDriver) {
       browser.goTo("/")
       val typeahead = browser.getDriver.findElement(By.id("municipality"))
       val button = browser.getDriver.findElement(By.id("reset-button"))
       typeahead.click
       typeahead.sendKeys("Saskatoon")
       val input = typeahead.getAttribute("value")
       assert(input must contain("Saskatoon"))
       button.click
       typeahead.getText must beEmpty
    }
    
    
    
    
}
