package util

import models._
import scala.util.{Try, Success, Failure}
import play.api.libs.json._

/**
 * Contains methods for performing and simplifying type conversions.
 */
object TypeConversions {
  /**
   * Converts a list of SlimLocations into a JSON array. Output is in a format like:
   *
   * {{{
   *   [
   *     {
   *       "name": "7 Eleven",
   *       "address": "123 Fake St",
   *       "id": 456
   *     }
   *   ]
   * }}}
   */
  def slimLocationsToJson(locations: Seq[SlimLocation]): JsValue = {
    val jsObjects = locations.map { location =>
      Json.obj(
        "name" -> location.name,
        "address" -> location.address,
        "id" -> location.id
      )
    }

    Json.toJson(jsObjects)
  }
}