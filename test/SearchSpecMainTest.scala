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

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class SearchSpecMainTest extends Specification {
  /* 
   * Basic check that all views are being rendered in html
   */
  "Searching" should {
    "display search page" in new WithBrowser{
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon Palace")
      
      // Make sure that correct input is in the typeahead
      val input = typeahead.getAttribute("value")
      input must contain("Golden Dragon Palace")
      typeahead.sendKeys(Keys.ENTER)
      browser.title() must contain("EatSafe search")//I cant seem to use Messages here because they are dynamically generated
    }
    
    /*
     * There should be two results in this case, because there are two locations that have the word 'Golden' in the name
     */
    "display the proper results for a !lax search" in new WithBrowser{
      //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden")
      typeahead.sendKeys(Keys.ENTER)
      
      //there should be 2 results on the page
      browser.pageSource() must contain(" of 2 results")//I cant seem to use Messages here because they are dynamically generated
    }

    "display the proper results when searching by address" in new WithBrowser{
      //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("505 Grand Avenue")
      typeahead.sendKeys(Keys.ENTER)
      
      //there should be 2 results on the page
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated
      
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))
      locationLink.click
      
      browser.title() must contain("Golden Dragon Palace [505 Grand Avenue]")
      
    }

    "display the proper results for a lax search" in new WithBrowser{
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon")
      typeahead.sendKeys(Keys.ENTER)
      
      //there should be 1 results on the page because default !lax search looks for 'Golden'&'Dragon'
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated
      val laxSearchButton = browser.getDriver.findElement(By.linkText(Messages("locations.search.weakSearch2")))
      laxSearchButton.click
      
      //there should be 2 results on the page because lax search looks for names containing 'Golden' OR 'Dragon'
      browser.pageSource() must contain(" of 2 results")
    }
    
    "displayed links should go to the right pages" in new WithBrowser{
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden Dragon")
      typeahead.sendKeys(Keys.ENTER)
      
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))
      locationLink.click
      
      browser.title() must contain("Golden Dragon Palace [505 Grand Avenue]")
    }
    
    "display no results" in new WithBrowser{
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Goldan Dragan Palace")
      typeahead.sendKeys(Keys.ENTER)
      val noRes = browser.getDriver.findElement(By.xpath("//tbody//tr"))
      noRes.getText must contain(Messages("locations.search.noResults"))
      
      
      
    }
    
    /*
     * Results should be the same when searching for "505 Grand Ave" and "505 Grand Avenue"(done above) 
     */
    "display results searching with aliases" in new WithBrowser{
       //search for 'Golden' in Luseland
      browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("505 Grand Ave")
      typeahead.sendKeys(Keys.ENTER)
      //there should be 1 results on the page
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated    
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))//make sure the link exists
           
    }
    
    "handle lots of spaces" in new WithBrowser {
       browser.goTo("/find/luseland")
      val typeahead = browser.getDriver.findElement(By.id("location"))
      typeahead.click
      typeahead.sendKeys("Golden       Dragon                       Palace")
      typeahead.sendKeys(Keys.ENTER)
      //there should be 1 results on the page
      browser.pageSource() must contain(" of 1 results")//I cant seem to use Messages here because they are dynamically generated    
      val locationLink = browser.getDriver.findElement(By.linkText("Golden Dragon Palace"))//make sure the link exists
    }
    
    
    
    
    
  }
}

