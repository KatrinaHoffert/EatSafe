package util

import models._
import scala.util.{Try, Success, Failure}
import play.api.libs.json._
import play.api.i18n.Messages

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
      val address = location.address.getOrElse(Messages("errors.unknownAddress"))
      Json.obj(
        "name" -> location.name,
        "address" -> address,
        "id" -> location.id
      )
    }

    Json.toJson(jsObjects)
  }

  /**
   * Converts a list of Locations into JSON with everything the multi-map needs: names, coords,
   * rating colors, and IDs.
   *
   * {{{
   *   [
   *     {
   *       "name": "Tiny Tina's Badass Grill",
   *       "address": "123 Fake St",
   *       "ratingColor": "#ff0000",
   *       "latitude": 52.1333,
   *       "longitude": -106.6833,
   *       "id": 123
   *     }
   *   ]
   * }}}
   *
   * @param locations A list of locations that MUST have coordinates. It's also assumed that addresses
   * all exist (otherwise coordinates shouldn't).
   */
  def locationsToMultiMapJson(locations: Seq[Location]): JsValue = {
    val jsObjects = locations.map { location =>
      Json.obj(
        "name" -> location.name,
        "address" -> location.address.get,
        "ratingColor" -> location.rating.color,
        "latitude" -> location.latitude.get,
        "longitude" -> location.longitude.get,
        "id" -> location.id
      )
    }

    Json.toJson(jsObjects)
  }
}