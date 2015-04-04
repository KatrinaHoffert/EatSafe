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
import org.openqa.selenium.ie.InternetExplorerDriver

import org.openqa.selenium.support.ui.Select

import play.api.i18n.Messages

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class IESpecBrowserTest extends Specification {
  
    //IE driver path
    System.setProperty("webdriver.ie.driver", "webDrivers/IEDriverServer.exe");
  
    "All pages should be able to access 'About' Page" in new WithBrowser(new InternetExplorerDriver) {
   //find city 
      
    browser.goTo("/")
    val action = new Actions(browser.getDriver)
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
    
    //find location
    browser.goTo("/find/Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))

    //show location page
    browser.goTo("/view/1")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))   
   
    //500 error page
    browser.goTo("/view/1000000")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
    
    //find city error page
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
    
        
      //400 error page
    browser.goTo("/view/99999999999999999")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
    
    //404 error page
    browser.goTo("/dfgjnsddjdk")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
    
    //search page
    browser.goTo("/search/saskatoon?q=subway")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.pageSource must contain (Messages("about.title"))
  }  
  
  "All pages should have link to 'Creative Commons' Page" in new WithBrowser(new InternetExplorerDriver) {
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
     
    //400 error page
    browser.goTo("/view/99999999999999999")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
    
    //404 error page
    browser.goTo("/dfgjnsddjdk")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
    
        //search results page
    browser.goTo("/search/saskatoon?q=subway")
    action.moveToElement(browser.webDriver.findElement(By.linkText("CC-BY-ND"))).perform
  }  
  
  
  
  "All pages should be able to access get back to First Page" in new WithBrowser(new InternetExplorerDriver) {
   //find city
    browser.goTo("/")
    val action = new Actions(browser.getDriver)
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //find location
    browser.goTo("/find/Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")

    //show location page
    browser.goTo("/view/1")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")   
    
    //500 error page
    browser.goTo("/view/1000000")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //find city error page
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //find city error page there are 2 ways to get back from here
    browser.goTo("/find/daDerpDaDerp")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("errors.emptyCityTryAgain")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //display map page
     browser.goTo("/map?address=610+2nd+Ave+N&city=Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //about page
    browser.goTo("/about")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //400 error page
    browser.goTo("/view/99999999999999999")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //404 error page
    browser.goTo("/dfgjnsddjdk")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    //search page
    browser.goTo("/search/saskatoon?q=subway")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
    
    // multi-map page
    browser.goTo("/citymap?city=Saskatoon")
    action.moveToElement(browser.webDriver.findElement(By.linkText(Messages("general.applicationName")))).perform
    action.click.perform
    Thread.sleep(100)
    browser.url must contain ("/")
  }
    

  "language selection" should {
    "change language for other pages" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("eo")
      Thread.sleep(1000)
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain("EatSafe Saskaĉevano")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      val input = typeahead.getAttribute("value")
      input must contain("saskatoon")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/saskatoon")
      browser.webDriver.findElement(By.className("smallHeading")).getText must contain("EatSafe Saskaĉevano")
    }
  }    

  "select city typeahead" should {

    "give error message when trying submit without input" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(100)
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.badInput"))
    }

    "give error page when trying to search for invalid place" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("asdfghjkl")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("asdfghjkl")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(100)
      browser.$(".topViewError").getText must contain(Messages("locations.selectCity.badInput"))
    }

    "display choose location page when location is typed in all caps" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("SASKATOON")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/SASKATOON")
      browser.pageSource must contain(Messages("locations.selectLocation.title"))//got to the next page, not error page
    }

    "display choose location page when location is typed in all lowercase" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("saskatoon")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/saskatoon")
      browser.pageSource must contain(Messages("locations.selectLocation.title"))//got to the next page, not error page
    }

    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskatoon")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, hint is clicked" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      val action = new Actions(browser.getDriver)
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.className("typeahead-display"))
      action.moveToElement(element)
      action.click
      action.perform
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, tab is pressed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, right is pressed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, down then tab is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.TAB)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, down then right arrow is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed and down then enter arrow is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, tab is pressed and submitted with button" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.TAB)
      button.click
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "display choose location page when location is partially typed, right is pressed and submitted with button" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("submitButton"))
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskato")
      
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      button.click
      Thread.sleep(1000)
      browser.url must contain("/find/Saskatoon")
    }

    "clear text field with clear typeahead button is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val button = browser.getDriver.findElement(By.id("reset-button"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Saskatoon")
      
      button.click
      typeahead.getText must beEmpty
    }
  }

  "select location page typeahead" should {
    
    "display location page when place is typed in all caps" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("TACO TIME")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("TACO TIME")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Taco Time")
      browser.title must contain(Messages("locations.view.titleStart"))//made it to not an aerror page
    }
    
    "display location page when location is typed in all lowercase" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("taco time")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("taco time")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Taco Time")
      browser.title must contain(Messages("locations.view.titleStart"))
    }
    
    "display location page when location is fully typed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Taco Time")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Taco Time")
      
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Taco Time")
      browser.title must contain(Messages("locations.view.titleStart"))
    }
 
    "display location page when location is partially typed, hint is clicked" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      val action = new Actions(browser.getDriver)
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")   
      
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.tagName("li"))
      action.moveToElement(element).perform
      action.click.perform
      Thread.sleep(1000)
      browser.url must contain("/view/")
      browser.pageSource must contain("Subway")
      browser.title must contain(Messages("locations.view.titleStart"))
    }
  
    "display location page when location is partially typed, tab is pressed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.TAB)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Subw")
    }

    "display location page when location is partially typed, right is pressed and submitted with enter" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Subw")
    }

    "display location page when location is partially typed, down then tab is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.TAB)
      Thread.sleep(1000)
      browser.url must contain("/view/")
      browser.pageSource must contain("Subway")
    }

    "display location page when location is partially typed, down then right arrow is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      Thread.sleep(2000)
      browser.url must contain("/view/")
      browser.pageSource must contain("Subway")
    }

    "display location page when location is partially typed, down then enter arrow is pressed" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.ARROW_DOWN)
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.url must contain("/view/")
      browser.pageSource must contain("Subway")
    }


    "display location page when location is partially typed, tab is pressed and submitted with button" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      val button = browser.getDriver.findElement(By.className("typeahead-button"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.TAB)
      button.click
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Subw")
    }

    "display location page when location is partially typed, right is pressed and submitted with button" in new WithBrowser(new InternetExplorerDriver) {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      val button = browser.getDriver.findElement(By.className("typeahead-button"))
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Subw")
      
      typeahead.sendKeys(Keys.ARROW_RIGHT)
      button.click
      Thread.sleep(1000)
      browser.url must contain("/search/")
      browser.pageSource must contain("Subw")
    }


    "clear text field with clear typeahead button is pressed" in new WithBrowser(new InternetExplorerDriver) {
       browser.goTo("/find/Saskatoon")
       val typeahead = browser.getDriver.findElement(By.id("location"))
       val button = browser.getDriver.findElement(By.id("reset-button"))
       typeahead.click
       typeahead.sendKeys("Subway")
       
       // Make sure that correct input is in the typeahead
       val input = typeahead.getAttribute("value")
       input must contain("Subway")
       
       button.click
       typeahead.getText must beEmpty
    }

  }
  
  
       "Searching" should {
    "display search page" in new WithBrowser(new InternetExplorerDriver){
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon Palace")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Golden Dragon Palace")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      browser.title() must contain("EatSafe search")//I cant seem to use Messages here because they are dynamically generated
    }
    
    /*
     * There should be two results in this case, because there are two locations that have the word 'Golden' in the name
     */
    "display the proper results for a !lax search" in new WithBrowser(new InternetExplorerDriver){
      //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      
      //there should be 2 results on the page
      browser.pageSource() must contain(" of 2 results")//I cant seem to use Messages here because they are dynamically generated
    }

    "display the proper results when searching by address" in new WithBrowser(new InternetExplorerDriver){
      //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("505 Grand Avenue")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      
      //there should be 2 results on the page
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated
      
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))
      locationLink.click
      Thread.sleep(1000)
      browser.title() must contain("Golden Dragon Palace [505 Grand Avenue]")
      
    }

    "display the proper results for a lax search" in new WithBrowser(new InternetExplorerDriver){
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      //there should be 1 results on the page because default !lax search looks for 'Golden'&'Dragon'
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated
      val laxSearchButton = browser.getDriver.findElement(By.linkText(Messages("locations.search.weakSearch2")))
      laxSearchButton.click
      Thread.sleep(1000)
      //there should be 2 results on the page because lax search looks for names containing 'Golden' OR 'Dragon'
      browser.pageSource() must contain(" of 2 results")
    }
    
    "displayed links should go to the right pages" in new WithBrowser(new InternetExplorerDriver){
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))
      locationLink.click
      Thread.sleep(1000)
      browser.title() must contain("Golden Dragon Palace [505 Grand Avenue]")
    }
    
    "display no results" in new WithBrowser(new InternetExplorerDriver){
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Goldan Dragan Palace")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      val noRes = browser.getDriver.findElement(By.xpath("//tbody//tr"))
      noRes.getText must contain(Messages("locations.search.noResults"))
      
      
      
    }
    
    /*
     * Results should be the same when searching for "505 Grand Ave" and "505 Grand Avenue"(done above) 
     */
    "display results searching with aliases" in new WithBrowser(new InternetExplorerDriver){
       //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("505 Grand Ave")
      typeahead.sendKeys(Keys.ENTER)
      Thread.sleep(1000)
      //there should be 2 results on the page
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated    
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))//make sure the link exists
           
    }
   }
}
