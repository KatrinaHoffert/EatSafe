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
      System.out.println("Start 1 ")
      browser.goTo("/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      val typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
      typeaheadCity.click
      
      // Make a mistake and clear with button
      typeaheadCity.sendKeys("Waskatoon")
      typeaheadCity.getAttribute("value") must contain("Waskatoon")
      browser.webDriver.findElement(By.id("reset-button")).click
      typeaheadCity.getText must beEmpty
      
      System.out.println("Start 1 ")
      // Search for saskatoon
      typeaheadCity.sendKeys("Saskatoon")
      
      // Make sure that correct input is in the typeahead
      typeaheadCity.getAttribute("value") must contain("Saskatoon")    
      
      typeaheadCity.sendKeys(Keys.ENTER)
      browser.url must contain("/find/Saskatoon")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      System.out.println("Start 1 ")
      // on find location search for 2nd Avenue Grill
      val typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      typeaheadLoc.sendKeys("2nd Avenue Grill")
      System.out.println("Start 1 ")
      // Make sure that correct input is in the typeahead
      val input = typeaheadLoc.getAttribute("value")
      input must contain("2nd Avenue Grill")
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      
      browser.url must contain("/search/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      
      browser.webDriver.findElement(By.linkText("2nd Avenue Grill")).click
      
      browser.url must contain("/view/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      browser.pageSource must contain("2nd Avenue Grill")
      System.out.println("Start 1 ")
      // Try to go to the map page
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      browser.url must contain("map")
      browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed
      
      // go back to location page
      browser.webDriver.navigate.back
      browser.url must contain("/view/")
      browser.pageSource must contain("2nd Avenue Grill")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      
      System.out.println("Start 1 ")
      // open a past inspection
      browser.webDriver.findElement(By.id("showHideLink")).click
      browser.webDriver.findElement(By.id("pastInspections")).isDisplayed
      
      browser.webDriver.findElement(By.className("panel-title-text")).click
      
      browser.webDriver.findElement(By.className("panel-body")).isDisplayed
      System.out.println("Start 1 ")
      // go back to home page
      browser.webDriver.findElement(By.linkText("EatSafe Saskatchewan")).click
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      browser.url must equalTo("/")
    }
    
    "change language and make many bad choices" in new WithBrowser {
      System.out.println("Start 2 ")
      browser.goTo("/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      browser.url must equalTo("/")
      
      // Change the language
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("zh")
            System.out.println("Start 2 ")

      // Enter a bad city
      var typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
      typeaheadCity.click
      
      typeaheadCity.sendKeys(Keys.ENTER)

      // Check that error page has been reached
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.badInput")(Lang("zh")))
      browser.webDriver.findElement(By.className("footer")).isDisplayed
            System.out.println("Start 2 ")

      // clear typeahead
      browser.webDriver.findElement(By.id("reset-button")).click
      typeaheadCity.getText must beEmpty
      
      // Send wrong thing
      typeaheadCity.click
      typeaheadCity.sendKeys("Waskatoon")
      typeaheadCity.getAttribute("value") must contain("Waskatoon")
      
      typeaheadCity.submit
      
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.badInput")(Lang("zh")))
      
      // try again
      browser.url must equalTo("/")
      
      typeaheadCity = browser.getDriver.findElement(By.id("municipality"))
            System.out.println("Start 2 ")

      // clear typeahead
      browser.webDriver.findElement(By.id("reset-button")).click
      typeaheadCity.getText must beEmpty
      
      typeaheadCity.click
      
      typeaheadCity.sendKeys("Regina")
      typeaheadCity.getAttribute("value") must contain("Regina")   
      typeaheadCity.sendKeys(Keys.ENTER)
      
      browser.url must equalTo("/find/Regina")
             System.out.println("Start 2 ")

      // on find location search for random stuff
      var typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      typeaheadLoc.sendKeys("doesthisplaceexistsnookay")
      
      // Make sure that correct input is in the typeahead
      val input = typeaheadLoc.getAttribute("value")
      input must contain("doesthisplaceexistsnookay")
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      
      browser.url must contain("/search/")
      browser.webDriver.findElement(By.className("btn-primary")).click
      browser.url must equalTo("/find/Regina")
            System.out.println("Start 2 ")

      
      // Get impatient and try to guess at an id
      browser.goTo("/view/-45675")
      browser.url must equalTo("/view/-45675")
      browser.pageSource must contain(Messages("errors.error500Title")(Lang("zh")))
      
      // Try another one
      browser.goTo("/view/999999999999999")
      browser.url must equalTo("/view/999999999999999")
      browser.pageSource must contain(Messages("errors.error400Title")(Lang("zh")))
      
      // Try one last thing
      browser.goTo("/dfgjnsddjdk")
      browser.url must equalTo("/dfgjnsddjdk")
      browser.pageSource must contain(Messages("errors.error404Title")(Lang("zh")))
            System.out.println("Start 2 ")

      // Go back to search location
      browser.webDriver.navigate.back
      browser.webDriver.navigate.back
      browser.webDriver.navigate.back
      browser.webDriver.navigate.back
      browser.webDriver.navigate.back
            System.out.println("Start 2 ")

      typeaheadLoc = browser.getDriver.findElement(By.id("location"))
      // find some location containing s
      typeaheadLoc.sendKeys("Five Guys")
      
      // Make sure that correct input is in the typeahead
      typeaheadLoc.getAttribute("value") must contain("Five Guys")
      
      typeaheadLoc.sendKeys(Keys.ENTER)
      
      browser.url must contain("/search/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      
      browser.webDriver.findElement(By.linkText("Five Guys")).click
      
      browser.url must contain("/view/")
      browser.webDriver.findElement(By.className("footer")).isDisplayed
            System.out.println("Start 2 ")

      // Try to go to the map page
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      browser.url must contain("map")
      browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed
     
      // Get bored and refresh the page
      
      browser.webDriver.navigate.refresh
           System.out.println("Start 2 ")

      // go back to home page
      browser.webDriver.findElement(By.linkText(Messages("general.applicationName")(Lang("zh")))).click
      browser.webDriver.findElement(By.className("footer")).isDisplayed
      browser.url must equalTo("/")
      
      // Go to the about page
      browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")(Lang("zh")))).click
      browser.pageSource must contain (Messages("about.title")(Lang("zh")))
      
    }
  }
}