import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import models._
import scala.util._
import play.api.db._
import java.sql._
import play.api.Play.current
import org.specs2.mock._
import globals.ActiveDatabase

/**
 * Tests for the SearchEngine functionality of the database and model
 */
@RunWith(classOf[JUnitRunner])
class SearchEngineSpecMainTest extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("test")
  "Search Engine" should {
    "toTsQuery should replace spaces properly" in {
      val lax = SearchEngine.toTsQuery("this has spaces", true)
      lax must beEqualTo("this|has|spaces")
      val notLax = SearchEngine.toTsQuery("this has spaces", false)
      notLax must beEqualTo("this&has&spaces")
    }
    
    "escapeQuery should escape illegal values" in {
      val esc = SearchEngine.escapeQuery("""\ ! : | & ( )""")
      esc must beEqualTo("""\\ \! \: \| \& \( \)""")
    }
    
    "queryLocations" in new WithApplication{
      //strict search for My Place should have 2 results because both have My Place as an address
      var searchResults = SearchEngine.queryLocations("My Place", "Town A", false)
      searchResults.get.length must beEqualTo(2)
      //only one location contains "Andrews Kitchen" for a strict search
      searchResults = SearchEngine.queryLocations("Andrews Kitchen", "Town A", false)
      searchResults.get.length must beEqualTo(1)
      searchResults.get.head.name must beEqualTo("Andrews Kitchen")
      //for a lax search, "Andrews Kitchen should come up with 2 results, because "Andrews" is in both
      searchResults = SearchEngine.queryLocations("Andrews Kitchen", "Town A", true)
      searchResults.get.length must beEqualTo(2)    
      //Unknown city, with this search it should be empty
      searchResults = SearchEngine.queryLocations("Andrews Kitchen", "Unknown City", false)
      searchResults.get.length must beEqualTo(0)

      
      
      
    }
  } 
  
}
