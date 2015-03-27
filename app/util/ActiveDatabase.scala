package util

/**
 * A special class to be used implicitly by any method that accesses the database
 * this class simply holds the name of a database stored in the framework. (see conf/application.conf)
 * the purpose if to provide testing mechanism so that a testing database can be used for testing and a
 * deployment or default database is used for everything else
 */
class ActiveDatabase(val name: String)