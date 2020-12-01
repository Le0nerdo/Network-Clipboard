# Network Clipboard

![Badge telling if Mvane tests pass.](https://github.com/Le0nerdo/Network-Clipboard/workflows/Java%20CI%20with%20Maven/badge.svg)
[![codecov](https://codecov.io/gh/Le0nerdo/Network-Clipboard/branch/main/graph/badge.svg?token=H0Z401L8CI)](https://codecov.io/gh/Le0nerdo/Network-Clipboard)

> In the current version the application freezes when the internetconnection is lost. Also if there is no connection on allpication lauch the application needs to be restarted or "connect" option be used. These are known issues that I am working on.

Network Clipboard is a program that makes it possible to cut, copy and paste text across multiple devices.

## Features
* When using cut or copy on text the computer sends the data to a database, where other connected devices can read it from and with that update their clipboards.
* Selecting text from the history to be on the clipboard.
* Pause the system so that the clipboard on a certain devise will not be updated from the database, but it still writes to it.
* Stop the program so that no sending to or receiving from the database occurs, but you can still access the current history.
* Graphical user interface.

## Getting Started
* Make a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) user (or use existing one)
* Make a Cluster with a user to it that can write and read (for new users: complete the tutorial)
* Get the access link from "clusters > Connect > Connect your application" with the options Driver=Java and Version="4.1 or later".
* After starting the application put the access link into the text field next to the connect button and press connect. (The access link is stored in user preferences, so you dont have to enter it again)
* Do the same with all computers that you want to connect together, with links that connect to the same cluster. (you can use the same link on them all)
* **Remember to allow all the devices ip addresses to connect to the database under "Network Access" on the left menu on the MongoDB Atlas website!**

## Requirements
* Windows 10 or debian based linux
* Java 11 (or newer that is backwards compatible)
* Abte to use [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) 

## Limitations
* Only plain text works. When cutting or copying files they can not be pasted on other devices.

## Further Development Ideas
* Extending to work with some file types (not only plain text).
* Making the program with something more commonly found on devices than java.

## Development
The program can be started with the main method in [defaultpackage.Main.java](https://github.com/Le0nerdo/Network-Clipboard/blob/main/src/main/java/defaultpackage/Main.java).

### Run tests
```shell
mvn test
```

### Run checkstyle
```shell
mvn jxr:jxr checkstyle:checkstyle
```

### Run Jacoco (test coverage)
```shell
mvn clean jacoco:prepare-agent install jacoco:report
```