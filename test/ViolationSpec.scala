import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import assets.Violation

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class ViolationSpec extends Specification {

    val x = new Violation("violation description", "priority")
  "Violation" should {
      "begin with violation" in {
        x.description must startWith("violation");
      }
    }
}
