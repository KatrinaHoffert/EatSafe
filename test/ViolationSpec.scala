import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import models.Violation

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class ViolationSpec extends Specification {
   val violation = new Violation(123, "name", "violation description", "priority")
  "Violation" should {
    "begin with violation" in {
      violation.description must startWith("violation");
    }
  }
}
