# Network Clipboard

![Badge telling if Mvane tests pass.](https://github.com/Le0nerdo/Network-Clipboard/workflows/Java%20CI%20with%20Maven/badge.svg)
[![codecov](https://codecov.io/gh/Le0nerdo/Network-Clipboard/branch/main/graph/badge.svg?token=H0Z401L8CI)](https://codecov.io/gh/Le0nerdo/Network-Clipboard)

Network Clipboard is a program that makes it possible to cut, copy and paste text across multiple devices.

## Features
* When using cut or copy on text the computer sends the data to a _, where other connected devices can read it from and with that update their clipboards.
* Selecting text from the history to be on the clipboard.
* Pause the system so that the clipboard on a certain devise will not be updated from the _, but it still writes to it. (to be implemented)
* Stop the program so that no sending to or receiving from the _ occurs, but you can still access the current history. (to be implemented)
* Graphical user interface.

## Getting Started
* Make a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) user (or use existing one)
* Make a Cluster with a user to it that can write and read (for new users: complete the tutorial)
* Get the access link from "clusters > Connect > Connect your application" with the options Driver=Java and Version="4.1 or later".
* Make a ".env" file in the projects root folder with the content (replace "mongoDBAtlasLink" with the access link):
```
DB_URI=mongoDBAtlasLink
```
* Run the maven project
* Do the same with all computers that you want to connect together, with links that connect to the same cluster. (you can use the same link on them all)

## Requirements
* Windows 10 or debian based linux
* Java 11 (or newer that is backwards compatible)
* Abte to use [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) 

## Limitations
* Only plain text works. When cutting or copying files they can not be pasted on other devices.

## Further Development Ideas
* Extending to work with some file types (not only plain text).
* Making the program with something more commonly found on devices than java.
