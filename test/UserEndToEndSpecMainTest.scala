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

import play.api.i18n.Messages
import play.api.i18n.Lang

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
      
      // on find location search for 2nd Avenue Grill
      val typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      typeaheadLoc.sendKeys("2nd Avenue Grill")
      
      // Make sure that correct input is in the typeahead
      val input = typeaheadLoc.getAttribute("value")
      assert(input must contain("2nd Avenue Grill"))
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      
      assert(browser.url must contain("/search/"))
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      browser.webDriver.findElement(By.linkText("2nd Avenue Grill")).click
      
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
      val link = browser.webDriver.findElement(By.className("violationLink"))
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
    
    "change language and make many bad choices" in new WithBrowser {
      browser.goTo("/")
      (browser.webDriver.findElement(By.className("footer")).isDisplayed)
      (browser.url.equals("/"))
      
      // Change the language
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("eo")
      
      // Enter a bad city
      var typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
      typeaheadCity.click
      
      typeaheadCity.sendKeys(Keys.ENTER)

      // Check that error page has been reached
      (browser.$(".topViewError").getText must contain(Messages("locations.selectCity.noInput")(Lang("eo"))))
      (browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // Send wrong thing
      typeaheadCity.click
      typeaheadCity.sendKeys("Waskatoon")
      (typeaheadCity.getAttribute("value") must contain("Waskatoon"))    
      
      typeaheadCity.sendKeys(Keys.ENTER)
      
      // Check that error page has been reached
      (browser.webDriver.findElement(By.className("footer")).isDisplayed)
      (browser.url.equals("/find/Waskatoon"))
      (browser.pageSource contains(Messages("errors.emptyCityText")(Lang("eo"))))
      
      // Return to try again
      browser.webDriver.findElement(By.linkText(Messages("errors.emptyCityTryAgain")(Lang("eo")))).click
      (browser.webDriver.findElement(By.className("footer")).isDisplayed)
      (browser.url.equals("/"))
      
      typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
      typeaheadCity.click
      typeaheadCity.sendKeys("Regina")
      (typeaheadCity.getAttribute("value") must contain("Regina"))    
      typeaheadCity.sendKeys(Keys.ENTER)
      
      (browser.url.equals("/find/Regina"))
       
      // on find location search for random stuff
      var typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      typeaheadLoc.sendKeys("doesthisplaceexistsnookay")
      
      // Make sure that correct input is in the typeahead
      val input = typeaheadLoc.getAttribute("value")
      (input must contain("doesthisplaceexistsnookay"))
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      (browser.$(".topViewError").getText must contain(Messages("locations.selectLocation.badInput")(Lang("eo"))))
      
      // Get impatient and try to guess at an id
      browser.goTo("/view/-45675")
      (browser.url.equals("/view/-45675"))
      (browser.pageSource contains(Messages("errors.error500Title")(Lang("eo"))))
      
      // Try another one
      browser.goTo("/view/99999999999999999")
      (browser.url.equals("/view/99999999999999999"))
      (browser.pageSource contains(Messages("errors.error500Title")(Lang("eo"))))
      
      // Go back to search location
      browser.webDriver.navigate.back
      browser.webDriver.navigate.back
      
      typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      // find some location containing s
      typeaheadLoc.sendKeys("Five Guys")
      
      // Make sure that correct input is in the typeahead
      (typeaheadLoc.getAttribute("value") must contain("Five Guys"))
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      (browser.url must contain("/view/"))
      (browser.webDriver.findElement(By.className("footer")).isDisplayed)
      
      // Try to go to the map page
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      (browser.url must contain("map"))
      (browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
     
      // Get bored and refresh the page
      
      browser.webDriver.navigate.refresh
     
      // go back to home page
      browser.webDriver.findElement(By.linkText(Messages("general.applicationName")(Lang("eo")))).click
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      assert(browser.url.equals("/"))
      
      // Go to the about page
      browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")(Lang("eo")))).click
      browser.pageSource must contain (Messages("about.title")(Lang("eo")))
      
    }
  }
}