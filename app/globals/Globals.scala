package globals

class ActiveDatabase(val name: String)

package object Globals {
  implicit lazy val db = new ActiveDatabase("default")
}