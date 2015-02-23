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
class ModelSpec extends Specification with Mockito {

  // Run test functions here
  this.getViolationsTests
  this.getInspectionsTests
  this.getLocationByIdTests
  this.getLocationsByCityTests

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

      // TODO: There should be a test where the database connection is not available, but we need some
      // kind of test hook in order to do this, perhaps pass in the database connection or
      // something, so it can be mocked out to throw errors
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

  def getViolationsTests = {
    "getViolations" should {
      "return success when given proper inputs" in new WithApplication {
        // TODO get good data
        val violationList = Violation.getViolations(2)
        violationList.get
      }

      "given a certain ID, the number of returned violations should be correct" in new WithApplication {
        val violationList = Violation.getViolations(31)
        violationList.get.length must beEqualTo(3)
      }

      "return a failure if there is a bad database" in new WithApplication  {
        val vioList = Violation.getViolations(1)((new ActiveDatabase("BadDatabase")))
        vioList.isFailure mustEqual true
      }
    }
  }

  def getInspectionsTests = {
    "getInspections" should {
      "return success when given proper inputs" in new WithApplication {
        val inspectionList = Inspection.getInspections(2)
        inspectionList.get
      }

      "given a good id, return the right amount of inspections" in new WithApplication {
        val inspectionList = Inspection.getInspections(15)
        inspectionList.get.size must beEqualTo(3)
      }

      "return a failure if there is a bad database" in new WithApplication  {
        val inspectionList = Inspection.getInspections(15)((new ActiveDatabase("badDatabase")))
        inspectionList.isFailure mustEqual true
      }
    }
  }
}
