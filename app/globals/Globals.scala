package object globals {
  /**
   * Supplies a database name. Used by functions that must access a database so that they know
   * which database to access. In particular, the tests can then use a testing database while
   * production can use a different one (see the conf/application.conf file for the mappings
   * of database name to credentials/URL).
   *
   * Simply importing this package object (ie, with `import globals._`) is enough to allow using
   * methods requiring an implicit ActiveDatabase.
   *
   * Tests should use an *explicit* ActiveDatabase so that other globals could be used.
   */
  implicit lazy val defaultDb = new ActiveDatabase("default")
}