package util

import models._
import scala.util.{Try, Success, Failure}
import play.api.libs.json._
import play.api.i18n.Messages
import play.api._
import play.api.mvc._

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
      val jsObj = Json.obj(
        "name" -> location.name,
        "ratingColor" -> location.rating.color,
        "latitude" -> location.latitude.get,
        "longitude" -> location.longitude.get,
        "id" -> location.id
      )

      if(location.address.isDefined) {
        jsObj ++ Json.obj("address" -> location.address.get)
      }
      else jsObj
    }

    Json.toJson(jsObjects)
  }

  /**
   * Extends a sequence of strings to the format that selects require (in the form helpers), which
   * is a pair of values and displayed text. This method sets the value and displayed text to be the
   * same.
   */
  def extendSeqToSelect(seq: Seq[String]): Seq[(String, String)] = {
    seq.map { value =>
      (value, value)
    }
  }

  /**
   * Like extendSeqToSelect(Seq[String]), but it takes separate values and displayed text (which must
   * be sequences of the same size).
   */
  def mapValuesToDisplayed(value: Seq[String], displayed: Seq[String]): Seq[(String, String)] = {
    assert(value.size == displayed.size)
    displayed.zip(value)
  }
}