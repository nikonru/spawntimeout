# About

* Minecraft: **1.20.1**
* Mod Launcher: **Forge**

A simple mod that allows you to configure spawn timeout on server.

# Build
## Requirements
To build this project you need `Gradle` version `8.8` and `Java SDK` version `22.0.2`.

You can check your `Java SDK` version with the following command:
```
javac -version
```
And your `Gradle` version with:
```
gradle -version
```

## Build Jar
Once all requirements are satisfied, run `Gradle` in your repository folder:
```
gradle build
```
This command creates a `build/libs` folder in your repository. You can find the `jar` file there.

# How it works

When a player dies he is put in immobilized state and to the spectator mod, then server teleports him to some predefined position where player waits for respawn. 
