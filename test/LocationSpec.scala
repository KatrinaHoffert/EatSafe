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
import globals.Globals.testDB
import globals.ActiveDatabase

/**
 * Test Suite of the current model, the tests being focused on the Locations class
 * and its methods, mostly because the other classes (Inspection and Violation) are simple
 * case classes with nothing to test but prebuilt getters and setters (we will assume scala knows
 * what its doing) the sets of tests are wrapped in functions in order to make things more
 * readable and reusable in the future.
 */
@RunWith(classOf[JUnitRunner])
class LocationSpec extends Specification with Mockito {

  // Run test functions here
  this.getLocationByIdTests
  this.getLocationsByCityTests
  this.listCitiesTests

  def getLocationByIdTests = {
    // Currently, this gets the default database source, in future, we can
    // easily add reference to a testing database in the play conf/application.conf
    // as for now, they will be the same data anyway so I wont worry
    "getLocationById" should {
      "return a success with proper data when given good data" in new WithApplication {
        // Try.get throws an exception if it's a Failure
        val goodLoc = Location.getLocationById(1)
        goodLoc.get
      }

      // Based on current database
      "return good data when given a good ID" in new WithApplication {
        val goodLoc = Location.getLocationById(7)
        val location = goodLoc.get
        location.id must beEqualTo(7)
        // Ignore space due to trailing whitespace
        location.name must beEqualTo("Burstall Curling Rink - Kitchen").ignoreSpace
        location.address must beEqualTo("Maharg Ave").ignoreSpace
        location.postalCode must beEqualTo("S0N 0H0").ignoreSpace
      }

      "return a failure if there is a bad database" in new WithApplication {
        val goodLoc = Location.getLocationById(7)((new ActiveDatabase("badDatabase")))
        goodLoc.isFailure mustEqual true
      }
    }
  }

  def getLocationsByCityTests = {
    "GetLocationsByCity" should {
      "return a success when given 'Saskatoon'" in new WithApplication {
        val goodLocList = Location.getLocationsByCity("Saskatoon")
        goodLocList.get
      }

      "return a sequence of Locations with 929 values when given 'Saskatoon'" in new WithApplication {
        val goodLocList = Location.getLocationsByCity("Saskatoon")
        goodLocList.get.length must beEqualTo(929)
      }

      "return a failure if there is a bad database" in new WithApplication  {
        val badLocList = Location.getLocationsByCity("Saskatoon")((new ActiveDatabase("badDatabase")))
        badLocList.isFailure mustEqual true
      }
    }
  }
  
  def listCitiesTests = {
    "listCities" should {
      "list all cities with resteraunts in saskatchewan" in new WithApplication {
        val listOfCities = Location.listCities();
        listOfCities.get.length must beEqualTo(465)
      }
      "list should be in alphabetical order" in new WithApplication {
        val listOfCities = Location.listCities();
        val citySeq = listOfCities.get
        citySeq.indexOf("Denzil") must beLessThan(citySeq.indexOf("Saskatoon")) // Luseland should appear before Saskatoon in the list
      }
      
    }
  }
}
