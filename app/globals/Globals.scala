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

  lazy val validRhas = Seq(
    "Saskatoon Health Authority",
    "PA Parkland Health Authority",
    "Regina QuAppelle Health Authority",
    "Sun Country Health Authority",
    "Prairie North Health Authority",
    "Sunrise Health Authority",
    "Kelsey Trail Health Authority",
    "Five Hills Health Authority",
    "Heartland Health Authority",
    "Cypress Health Authority",
    "Northern Health - Mamaw/Keewa/Athab"
  )

  lazy val validInspectionTypes = Seq(
    "Routine",
    "Follow-up",
    "General Inspection",
    "Complaint",
    "Special"
  )

  lazy val validInspectionTypesKeys = Seq(
    "locations.view.type.Routine",
    "locations.view.type.Follow-up",
    "locations.view.type.GeneralInspection",
    "locations.view.type.Complaint",
    "locations.view.type.Special"
  )

  lazy val validReinspectionPriorities = Seq(
    "Low",
    "Moderate",
    "High"
  )

  lazy val validReinspectionPrioritiesKeys = Seq(
    "admin.add.lowPriority",
    "admin.add.moderatePriority",
    "admin.add.highPriority"
  )
}