# Network Clipboard
> Testing with mongoDB Atlas, not sure what database to use.

Network Clipboard is a program that makes it possible to cut, copy and paste text across multiple devices with a clipboard history.

## Features
* When using cut or copy on text the computer sends the data to a _, where other connected devices can read it from and with that update their clipboards.
* Selecting text from the history to be on the clipboard. (to be implemented)
* Pause the system so that the clipboard on a certain devise will not be updated from the _, but it still writes to it. (to be implemented)
* Stop the program so that no sending to or receiving from the _ occurs, but you can still access the current history. (to be implemented)
* Graphical user interface. (to be implemented most likely with javaFX)

## Getting Started
After starting the program you need to set up the devices by logging in through Googles authentication system (same Google account on all devices).

## Requirements
* Windows 10 or debian based linux
* Java 11 (or newer that is backwards compatible)
* Google user

## Limitations
* Only plain text works. When cutting or copying files they can not be pasted on other devices.

## Further Development Ideas
* Extending to work with some file types (not only plain text).
* Making the program with something more commonly found on devices than java.
