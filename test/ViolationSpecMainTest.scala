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
import util.ActiveDatabase

/**
 * Test Suite of the current model, the tests being focused on the Locations class
 * and its methods, mostly because the other classes (Inspection and Violation) are simple
 * case classes with nothing to test but prebuilt getters and setters (we will assume scala knows
 * what its doing) the sets of tests are wrapped in functions in order to make things more
 * readable and reusable in the future.
 */
@RunWith(classOf[JUnitRunner])
class ViolationSpecMainTest extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("test")
  implicit lazy val connection = DB.getConnection("test") // Run test functions here
  this.getViolationsTests
 
  def getViolationsTests = {
    "getViolationsByID " should {
      "return the proper violation type for an id" in new WithApplication {
        val violation = Violation.getViolationById(6)
        violation.get.description must beEqualTo("parser.desc.6")
      }
      //this will show  that the flyweight is working and the violation is not being fetched from the db again
      "return valid data without db after filling Pool" in new WithApplication {
        val violation = Violation.getViolationById(6)(new ActiveDatabase("badDB"))
        violation.get.description must beEqualTo("parser.desc.6")
      }
    }
    "getViolations" should {
      "return success when given proper inputs" in new WithApplication {
        // TODO get good data
        val violationList = Violation.getViolations(2)
        violationList.get //throws error if bad
      }

      "given a certain ID, the number of returned violations should be correct" in new WithApplication {
        val violationList = Violation.getViolations(16)
        violationList.get.length must beEqualTo(1)
      }
      
     "return error when given illegal inputs" in new WithApplication {
        val violationList = Violation.getViolations(-1)
        violationList.get must throwA[IllegalArgumentException]
      }
    }
  }
}
