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


import globals.ActiveDatabase

import play.api.i18n.Messages

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpecMainTest extends Specification {
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
    
    "not crash with repeated refreshes" in new WithBrowser {
      browser.goTo("/")
      var i = 0
      for(i <- 1 to 10){
        browser.webDriver.navigate.refresh
      }
      assert(browser.webDriver.findElement(By.className("largeHeading")).isDisplayed)
      assert(browser.webDriver.findElement(By.className("footer")).isDisplayed)
      assert(browser.webDriver.findElement(By.className("typeahead-container")).isDisplayed)
    }
  }

  "language selection" should {
    "change language for other pages" in new WithBrowser {
      browser.goTo("/")
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("eo")
      assert(browser.webDriver.findElement(By.className("largeHeading")).getText contains("EatSafe Saskaĉevano"))
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      val input = typeahead.getAttribute("value")
      assert(input must contain("saskatoon"))
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/saskatoon"))
      assert(browser.webDriver.findElement(By.className("smallHeading")).getText contains("EatSafe Saskaĉevano"))
    }
  }
  
  "footer" should {
    "show about page when link clicked" in new WithBrowser {
      browser.goTo("/")
      val link = browser.webDriver.findElement(By.linkText(Messages("footer.aboutLink")))
      link.click()
      assert(browser.url contains("/about"))
    }
    
    "show creative commons page when link is clicked" in new WithBrowser {
      browser.goTo("/")
      val link = browser.webDriver.findElement(By.linkText("CC-BY-ND"))
      assert(link.getAttribute("href").contains("creativecommons"))
    }
    
    "able to select language from the drop down list" in new WithBrowser {
      browser.goTo("/")
      val selection = new Select(browser.webDriver.findElement(By.id("languageSelect")))
      selection.selectByValue("eo")
      assert(browser.webDriver.findElement(By.className("largeHeading")).getText contains("EatSafe Saskaĉevano"))
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
      assert(status(error) must equalTo(404))
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
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("asdfghjkl"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.pageSource must contain(Messages("errors.emptyCityDesc")))
    }
    
    "display choose location page when location is typed in all caps" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("SASKATOON")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("SASKATOON"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/SASKATOON"))
      browser.title() must contain(Messages("locations.selectLocation.title"))//made it to not an aerror page
    }
    
    "display choose location page when location is typed in all lowercase" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("saskatoon")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("saskatoon"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/saskatoon"))
      browser.title() must contain(Messages("locations.selectLocation.title"))
    }
    
    "display choose location page when location is fully typed and submitted with enter" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      typeahead.click
      typeahead.sendKeys("Saskatoon")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("Saskatoon"))      
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "display choose location page when location is partially typed, hint is clicked and submitted with enter" in new WithBrowser {
      browser.goTo("/")
      val typeahead = browser.getDriver.findElement(By.id("municipality"))
      val action = new Actions(browser.getDriver)
      typeahead.click
      typeahead.sendKeys("Saskato")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("Saskato"))
      
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.linkText("Saskatoon"))
      action.moveToElement(element)
      action.click
      action.perform
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/find/Saskatoon"))
    }
    
    "clear text field with clear typeahead button is pressed" in new WithBrowser {
       browser.goTo("/")
       val typeahead = browser.getDriver.findElement(By.id("municipality"))
       typeahead.click
       typeahead.sendKeys("Saskatoon")
       
       // Make sure correct information is in the typeahead
       val input = typeahead.getAttribute("value")
       assert(input must contain("Saskatoon"))
       
       val button = browser.getDriver.findElement(By.id("reset-button"))
       button.click
       typeahead.getText must beEmpty
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
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("2nd Avenue Grill"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
    }
    
   "display location page when place is typed in all caps" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("TACO TIME")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("TACO TIME"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("Taco Time"))
      browser.title() must contain(Messages("locations.view.titleStart"))//made it to not an aerror page
    }
    
    "display location page when location is typed in all lowercase" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("taco time")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("taco time"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("Taco Time"))
      browser.title() must contain(Messages("locations.view.titleStart"))
    }
    
    "display location page when location is fully typed and submitted with enter" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Taco Time")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("Taco Time"))
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("Taco Time"))
      browser.title() must contain(Messages("locations.view.titleStart"))
    }
    
    "display location page when location is partially typed, hint is clicked and submitted with enter" in new WithBrowser {
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      val action = new Actions(browser.getDriver)
      typeahead.click
      typeahead.sendKeys("Subw")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("Subw"))    
      
      action.moveToElement(typeahead).perform
      val element = browser.webDriver.findElement(By.partialLinkText("Subway"))
      action.moveToElement(element)
      action.click
      action.perform
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      assert(browser.pageSource contains("Subway"))
      browser.title() must contain(Messages("locations.view.titleStart"))
    }
    
    "clear text field with clear typeahead button is pressed" in new WithBrowser {
       browser.goTo("/find/Saskatoon")
       val typeahead = browser.getDriver.findElement(By.id("location"))
       val button = browser.getDriver.findElement(By.id("reset-button"))
       typeahead.click
       typeahead.sendKeys("Subway")
       
       // Make sure that correct input is in the typeahead
       val input = typeahead.getAttribute("value")
       assert(input must contain("Subway"))
       
       button.click
       typeahead.getText must beEmpty
    }
    
  }
  
  "display location page" should {
    "display show map page when address is clicked" in new WithBrowser {
      browser.goTo("/view/3675")
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      assert(browser.url must contain("map"))
      assert(browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
    }
    
    "display regional health authority link" in new WithBrowser {
      //Find a place in the Saskatoon Health Region
      browser.goTo("/find/Saskatoon")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("2nd Avenue Grill")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      assert(input must contain("2nd Avenue Grill"))      
      
      typeahead.sendKeys(Keys.ENTER)
      assert(browser.url must contain("/view/"))
      
      //Find the link to the health region web page
      val healthlink = browser.webDriver.findElement(By.id("rhaSiteLink"))
      assert(healthlink.getAttribute("href") must contain("saskatoonhealth"))
    }
  }
  
  "violation info page" should {
    "display a page with violation info for specific violation" in new WithApplication {
      var inspection = route(FakeRequest(GET, "/view/1/violation/1")).get
      assert(status(inspection) == OK)
      assert(contentAsString(inspection) contains("violation 1"))
      
      inspection = route(FakeRequest(GET, "/view/1/violation/11")).get
      assert(status(inspection) == OK)
      assert(contentAsString(inspection) contains("violation 11"))
    }
    
    "returns to view when go back button is pressed" in new WithBrowser {
      browser.goTo("/view/1/violation/8")
      assert(browser.url.equals("/view/1/violation/8"))
      assert(browser.pageSource contains("violation 8"))
      
      val button = browser.webDriver.findElement(By.tagName("button"))
      assert(button.isDisplayed)
      button.click
      
      assert(browser.url.equals("/view/1"))
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
      browser.goTo("/view/3675")//this page needs to have a map link
      val action = new Actions(browser.getDriver)
      action.moveToElement(browser.webDriver.findElement(By.id("mapForLocation"))).perform
      action.click.perform
      
      assert(browser.url must contain("map"))
      assert(browser.webDriver.findElement(By.className("mapLocation-header")).isDisplayed)
      assert(browser.pageSource must contain("maps.googleapis.com"))
    }
    
  "All pages should be able to access 'About' Page" in new WithBrowser {
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
  
  "All pages should have link to 'Creative Commons' Page" in new WithBrowser {
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
  
  
  }
  "All pages should be able to access get back to First Page" in new WithBrowser {
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

}
