package models

import scala.util.{Try, Success, Failure}

object Rating extends Enumeration {
  protected case class RatingVal(val name: String, val color: String) extends super.Val(name)
  implicit def valueToRatingVal(value: Value) = value.asInstanceOf[RatingVal]

  val Good = RatingVal("good", "#008000")
  val Fair = RatingVal("fair", "#ffcc00")
  val Poor = RatingVal("poor", "#FF0000")
  val Unknown = RatingVal("unknown", "#808080")
}