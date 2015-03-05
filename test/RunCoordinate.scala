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
import coordinatesGetter._

/**
 * This is not a test. Since a started application is needed to run the PopulateCoordinates,
 * use this to create one by "WithApplication" method.
 */
@RunWith(classOf[JUnitRunner])
class RunCoordinate extends Specification with Mockito {
  implicit lazy val db = new ActiveDatabase("default")

  // Run test functions here
  this.runPopulateCoordinates

  def runPopulateCoordinates = {
    "Run PopulateCoordinates" should {
      "to populate the coordinates for each location in database" in new WithApplication {
        val goodLoc = PopulateCoordinates.main()
      }
    }
  }
}
