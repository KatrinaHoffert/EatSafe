[![Build Status](https://magnum.travis-ci.com/MikeHoffert/EatSafe.svg?token=yXznwCPJBpA9S1h8k4E4&branch=master)](https://magnum.travis-ci.com/MikeHoffert/EatSafe)

# EatSafe

An app for avoiding botulism

## Installing

You must first install [Scala](http://www.scala-lang.org/download/). It's sufficient to just install
the Scala binary, as everything related to Typesafe Activator that you may need is included with
the repo. You'll need Scala 2.11.5 (later will probably work -- that's the current version at the
time of writing).

With Scala installed and in the system path, you simply have to use the following command while in
the project directory (the one this file is located in).

```
./activator ~run
```

The above command is for Linux systems. For Windows, use `./activator.bat` for the file (for
Cygwin on Windows, use the Linux file -- the Linux file has been slightly modified for better
Cygwin support).

If you exclude the `~run`, you'll enter the activator console, in which you can execute commands
such as `~run` in the context of the program (the above command is simply more direct). The `run`
command runs the server. The `~run` command does the same, but also automatically recompiles files
when you change them. This is the ideal way to run the server for development.

Note that the first run will be considerably slower than subsequent runs because Activator (actually
SBT under the hood) must find and download all dependencies.

Once SBT has compiled everything, you'll see a message like:

```
[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000
```

This means that everything worked and the server can be viewed in any web browser at
`http://localhost:9000/`.

## Setting up the database

The database is PostgreSQL and must be at least version 9.3.

First of all, inside `application.conf`, you must set the database URL, username, and password. If
you're only going to be running the application and not using the tests, you can just set the test
database to the same credentials as the default database (it can't be blank, but it won't be used
unless you try and run the tests).

Next, you must actually populate the database. Inside the database folder (which must be the working
directory), run the `CreateTables.sql` file, then `statements.sql` to populate it, followed by
`RestoreCoordinates.sql` to populate the coordinates from the pre-generated data.

The `eatsafe_synonyms.syn` file must be placed in the `/usr/share/postgresql/9.3/tsearch_data` folder,
where "9.3" is your PostgreSQL version. Finally, run `CreateSearchTable.sql` to create the necessary
tables for full text search.

With this done, everything should be able to run locally.

## Setting up the server for production mode

For Linux servers, convenient `startServer.sh` and `stopServer.sh` scripts have been provided in the
root project directory. The former will start the server with `nohup` so that it'll continue running
when you exit the SSH session. The start server script will also restart the server if it's currently
running.

Note that there will be a delay from starting the server before you'll be able to see the results,
since the script will get all necessary dependencies and compile the code, which may take a while.
The script returns control of the console to you immediately. See the `nohup.out` and `errors.log`
files for the normal output.

By default, the start server script will start the server on port 8080. You have three choices here:

1. Change this to start on port 80 (the normal HTTP port). This requires root permission.
2. Just access the site via port 8080 (eg, with `http://example.com:8080`). Suitable for quick
   setup.
3. [Redirect 80 to 8080](http://www.cyberciti.biz/faq/linux-port-redirection-with-iptables/). This
   is ideal if you don't want to have to run the server with sudo. Of course, actually redirecting
   the port would require sudo, but the application itself will not run under that user. This is
   the approach we used.

This is all you need for the server to be able to successfully run.

## Auxillary programs

We've provided some additional programs that are meant for retrieving and parsing the data source.
See [the wiki](https://github.com/MikeHoffert/EatSafe/wiki/Running-auxillary-programs) for more
information on what these programs are and how to run them.

Note that at the time of writing, the data source has been taken down, so the auxillary programs are
in limbo. It is not necessary to run these programs, as the data output has been provided.

## Files

* `app` - This is the folder where most of our development source code will be.
* `app/assets` - These are additional resources that are processed in some way by the application
  (eg, LESS files).
* `app/controllers` - These are the Scala [controllers](https://www.playframework.com/documentation/2.3.7/ScalaActions).
* `app/views` - These are the Play Framework templates, which will generate HTML.
* `app/models` - These are the Scala models (completing our MVC pattern).
* `conf/application.conf` - Program configuration file. All conf here is globally accessible.
  See [here](https://www.playframework.com/documentation/2.3.7/Configuration) for Play Framework
  configuration.
* `conf/routes` - The [routing file](https://www.playframework.com/documentation/2.3.7/ScalaRouting),
  which maps URL patterns to controllers.
* `project` - Contains a few SBT configuration files, most notably the plugins file, where we ca
  specify libraries to include.
* `public` - This folder will be fully accesible to the public. It's used for static resources such
  as stylesheets, images, and JavaScript.
* `target` - A completely automatically generated folder that contains the compiled code. Ignore it.
* `test` - Unit test files go here. They'll be automatically compiled and run when you run
  `./activator test`. The [CI server](https://magnum.travis-ci.com/MikeHoffert/EatSafe.svg?token=yXznwCPJBpA9S1h8k4E4&branch=master)
  will also run them.