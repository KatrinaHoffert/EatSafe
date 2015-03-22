import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.filter.FilterConstructor._
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select

@RunWith(classOf[JUnitRunner])
class UserEndToEndSpecMainTest extends Specification {
  
  "basic run through from start to finish" should {
    
    "be successful for location in Saskatoon" in new WithBrowser(){
      browser.goTo("/")
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      val typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
      typeaheadCity.click
      
      // Make a mistake and clear with button
      typeaheadCity.sendKeys("Waskatoon")
      assert(typeaheadCity.getAttribute("value") must contain("Waskatoon"))     
      browser.webDriver.findElement(By.id("reset-button")).click
      typeaheadCity.getText must beEmpty
      
      
      // Search for saskatoon
      typeaheadCity.sendKeys("Saskatoon")
      
      // Make sure that correct input is in the typeahead
      assert(typeaheadCity.getAttribute("value") must contain("Saskatoon"))      
      
      typeaheadCity.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // on find location search for Geo Tims
      val typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      typeaheadLoc.sendKeys("2nd Avenue Grill")
      
      // Make sure that correct input is in the typeahead
      val input = typeaheadLoc.getAttribute("value")
      assert(input must contain("2nd Avenue Grill"))
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      assert(browser.pageSource contains("2nd Avenue Grill"))
      
      // Try to go to the map page
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      assert(browser.url must contain("map"))
      assert(browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
      
      // go back to location page
      browser.webDriver.navigate.back
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("2nd Avenue Grill"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // open a past inspection
      browser.webDriver.findElement(By.id("showHideLink")).click
      assert(browser.webDriver.findElement(By.id("pastInspections")).isDisplayed)
      
      browser.webDriver.findElement(By.className("panel-title-text")).click
      
      // click on the inspection
      val link = browser.webDriver.findElement(By.id("violationLink"))
      assert(link.isDisplayed)
      link.click
      
      // make sure inspection more info page reached
      assert(browser.url must contain("/violation/"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // use the back button on page to return
      browser.webDriver.findElement(By.tagName("button")).click
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("2nd Avenue Grill"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // go back to home page
      browser.webDriver.findElement(By.linkText("EatSafe Saskatchewan")).click
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      assert(browser.url.equals("/"))
    }
    
  }
}