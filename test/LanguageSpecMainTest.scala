import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.filter.FilterConstructor._

import org.openqa.selenium._

import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select


import util.ActiveDatabase

import play.api.i18n.Messages
import play.api.i18n.Lang

@RunWith(classOf[JUnitRunner])
class LanguageSpecMainTest extends Specification{
  "language" should {
    "detect the cookies" in new WithBrowser {
      browser.goTo("/")
      
      // give the page a cookie for language zh
      val cookie = new Cookie("lang","zh","localhost", "/", null)
      browser.webDriver.manage.addCookie(cookie)
      
      browser.goTo("/")
      
      // make sure the site is now in zh
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      browser.goTo("/find/Saskatoon")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      // delete the cookie and refresh the page
      browser.webDriver.manage.deleteCookie(cookie)
      browser.webDriver.navigate.refresh
      
      // make sure the site is back in en
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("en")))
      browser.goTo("/")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("en")))
    }
    
    "be the same on every page after selection" in new WithBrowser {
      browser.goTo("/")
      
      // Select different language
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("zh")
      
      // Make sure language has been changed
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //find location
      browser.goTo("/find/Saskatoon")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
  
      //show location page
      browser.goTo("/view/1")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh"))) 
      
      //500 error page
      browser.goTo("/view/1000000")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //find city error page
      browser.goTo("/find/daDerpDaDerp")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //find city error page there are 2 ways to get back from here
      browser.goTo("/find/daDerpDaDerp")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //display map page
      browser.goTo("/map?address=610+2nd+Ave+N&city=Saskatoon")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //about page
      browser.goTo("/about")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //search results page
      browser.goTo("/search/saskatoon?q=subway")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
       
      //400 error page
      browser.goTo("/view/999999999999999")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      //404 error page
      browser.goTo("/dfgjnsddjdk")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      // multi-map page
      browser.goTo("/citymap?city=Saskatoon")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
    }

    "will be remembered between tabs" in new WithBrowser {
      browser.goTo("/")
      
      // Select different language
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("zh")
      
      // Make sure language has been changed
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      // open a new tab
      browser.webDriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t")
      
      // get the tabs currently open
      val openTabs = browser.webDriver.getWindowHandles
      
      // tab1 is the first tab and tab2 is the newly opened one
      val tab1 = openTabs.iterator.next
      val tab2 = openTabs.iterator.next
      
      // switch to the new tab and check language
      browser.webDriver.switchTo.window(tab2)
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("zh")))
      
      // Select different language
      val selection2 = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection2.selectByValue("en")
      
      // switch back to original tab and refesh
      browser.webDriver.switchTo.window(tab1)
      browser.webDriver.navigate.refresh
      
      // language should now be what was selected in the other tab
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain(Messages("general.applicationName")(Lang("en")))
    }
    
    "does not allow bad cookies" in new WithBrowser {
      browser.goTo("/")
      
      // add a bad cookie
      val cookie = new Cookie("lang","trigedasleng","localhost", "/", null)
      browser.webDriver.manage.addCookie(cookie)
      
      // try to access the site with a bad cookie
      try{
        browser.goTo("/") 
      }
      catch {
        case e: RuntimeException => true
      }
    }
  }
}