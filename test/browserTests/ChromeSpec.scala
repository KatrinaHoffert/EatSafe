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
class ChromeSpec extends Specification {
  
    //the chromeDriver needs to know where to look for the driver application currently its in EatSafe/chromedriver.exe
    System.setProperty("webdriver.chrome.driver", "webDrivers/chromedriver.exe");
    

    
    "display choose location page when location is typed in all caps" in new WithBrowser(new ChromeDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
  
}
