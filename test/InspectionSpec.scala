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
class InspectionSpec extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("test")

  // Run test functions here
  this.getInspectionsTests
  
  def getInspectionsTests = {
    "getInspections" should {
      "return success when given proper inputs" in new WithApplication {
        val inspectionList = Inspection.getInspections(2)
        inspectionList.get
      }

      "given a good id, return the right amount of inspections" in new WithApplication {
        val inspectionList = Inspection.getInspections(15)
        inspectionList.get.size must beEqualTo(0)
      }

      "return a failure if there is a bad database" in new WithApplication  {
        val inspectionList = Inspection.getInspections(15)((new ActiveDatabase("badDatabase")))
        inspectionList.isFailure mustEqual true
      }
    }
  }
}
