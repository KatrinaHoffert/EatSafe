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

You'll also find `./activator ~test` to be useful. It works like the above command, but instead just
runs tests.

Note that the first run will be considerably slower than subsequent runs because Activator (actually
SBT under the hood) must find and download all dependencies.

Once SBT has compiled everything, you'll see a message like:

```
[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000
```

This means that everything worked and the server can be viewed in any web browser at
`http://localhost:9000/`.

## Files

* `app` - This is the folder where most of our development source code will be.
* `app/assets` - These are additional resources that are processed in some way by the application.
* `app/controllers` - These are the Scala [controllers](https://www.playframework.com/documentation/2.3.7/ScalaActions).
* `app/views` - These are the Play Framework templates, which will generate HTML.
* `app/models` - These are the Scala models (completing our MVC pattern).
* `conf/application.conf` - Program configuration file. All conf here is globally accessible.
  See [here](https://www.playframework.com/documentation/2.3.7/Configuration) for Play Framework
  configuration and [here](http://stackoverflow.com/a/10534049/1968462) for accessing configuration
  values programmatically.
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

## Use with Scala IDE

* Download and install [Scala IDE](http://scala-ide.org/).
* Run `./activator ~run` in separate console. Scala IDE will be used only for editing and not
  for building.
* Run `./activator eclipse` before importing your project in order to generate a .classpath file 
  that is right for you!
* Import the project into Scala IDE with File > Import > Existing Project Into Workspace and choose
  the folder containing this file.
* In order to remove some compilation issues, you'll have to go to Project > Properties >
  Java Build Path > Libraries > Add external class library and add the `target/scala-2.11/classes_managed`
  folder. This ensures that Scala IDE can find the compiled templates.