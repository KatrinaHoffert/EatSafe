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
 * Test Suite of the current model, the tests being focused on the Locations class
 * and its methods, mostly because the other classes (Inspection and Violation) are simple
 * case classes with nothing to test but prebuilt getters and setters (we will assume scala knows
 * what its doing) the sets of tests are wrapped in functions in order to make things more
 * readable and reusable in the future.
 */
@RunWith(classOf[JUnitRunner])
class LocationSpecMainTest extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("test")

  // Run test functions here
  this.getLocationByIdTests
  this.getLocationsByCityTests
  this.listCitiesTests
  this.getAllLocationsWithCoordinatesTests

  def getAllLocationsWithCoordinatesTests = {
    "getAllLocationsWithCoordinates" should {
      "get all locations that have coords" in new WithApplication {
        val locsWithCorrds = Location.getAllLocationsWithCoordinates().get
        locsWithCorrds.length must beEqualTo(3);
        locsWithCorrds.foreach { x =>
          if(x.address.get == "South Park Ave")
          {
            x.latitude.get must beEqualTo(26.696545)
            x.longitude.get must beEqualTo(2.988281)
          }
          else
          {
            x.latitude.get must beEqualTo(1.0)
            x.longitude.get must beEqualTo(1.0)
          }
        }
        
      }
    }
  }
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
        location.name must beEqualTo("Hogwarts Dining Hall").ignoreSpace
        location.address.get must beEqualTo("Hogwarts Place").ignoreSpace
        location.postalCode.get must beEqualTo("S0G 2J0").ignoreSpace
        
        val locWithNulls = Location.getLocationById(8).get
        locWithNulls.name must beEqualTo("???")
        locWithNulls.address must beEqualTo(None)
        locWithNulls.city must beEqualTo(None)
        locWithNulls.postalCode must beEqualTo(None)
        
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

      "return a sequence of Locations with 2 values when given 'Town A'" in new WithApplication {
        val goodLocList = Location.getLocationsByCity("Town A")
        goodLocList.get.length must beEqualTo(2)
      }

      "return a failure if there is a bad database" in new WithApplication  {
        val badLocList = Location.getLocationsByCity("Town A")((new ActiveDatabase("badDatabase")))
        badLocList.isFailure mustEqual true
      }
    }
  }
  
  def listCitiesTests = {
    "listCities" should {
      "list all cities with resteraunts in saskatchewan" in new WithApplication {
        val listOfCities = Location.listCities();
        listOfCities.get.length must beEqualTo(6+1)//the plus 1 is for Unknown City
      }
      "list should be in location order" in new WithApplication {
        val listOfCities = Location.listCities();
        val citySeq = listOfCities.get
        // Cities are ordered by number of locations, so big cities must be earlier in the list
        citySeq.indexOf("Saskatoon") must beLessThan(citySeq.indexOf("Aberdeen"))
      }
      
    }
  }
}
