[![Build Status](https://magnum.travis-ci.com/MikeHoffert/EatSafe.svg?token=yXznwCPJBpA9S1h8k4E4&branch=master)](https://magnum.travis-ci.com/MikeHoffert/EatSafe)

# EatSafe Saskatchewan

EatSafe Saskatchewan is a web application for accessing health and safety records of locations in
Saskatchewan. It provides user and mobile friendly access to data that was previously hosted at
the Government of Saskatchewan's online restaurant inspection report site (http://orii.health.gov.sk.ca/).

The application also boasts features such as the ability to plot all locations on a map with colored
markers for their current rating (as determined by re-inspection priority). The intent of the
application is to provide easier and more user-friendly access to information than the ORII.

## Screenshots

![Selecting a location](http://i.imgur.com/SIhzBOJ.png)

![Multi-map](http://i.imgur.com/AAOsryV.png)

![Viewing a location](http://i.imgur.com/jl8Lh4F.png)

## Installing

See also: [abridged setup guide](https://github.com/MikeHoffert/EatSafe/wiki/Abridged-setup-guide)

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

The `eatsafe_synonyms.syn` file must be placed in the `/usr/share/postgresql/9.3/tsearch_data` folder,
where "9.3" is your PostgreSQL version.

Next, to actually populate the database, you must run `createDatabase.sh` inside the `database`
folder. Edit the file to add your DB credentials, first. This file can be re-run in the future to
update the database (which will completely overwrite the existing tables).

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

## Further resources

See [the wiki](https://github.com/MikeHoffert/EatSafe/wiki) for more information, including info on axuillary programs, the admin interface, and running the tests.
