package globals

package object Globals {
  
  /**
   * Any method that includes a implicit ActiveDatabase needs access to one of these implicit values
   * any classes that are not testing classes should import globals.Globals.defaultDB and while testing classes (in the test folder)
   * import globals.Globals.testDB
   */
  implicit lazy val defaultDB = new ActiveDatabase("default")
  
  implicit lazy val testDB = new ActiveDatabase("test")
}