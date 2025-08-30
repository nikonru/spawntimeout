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

When a player enters the server for the first time, the mod puts them into Spectator mode and lets them choose a spawn area.
Once the player selects an area, the mod tries random block positions on the area’s heightmap. 
When it finds a valid position, it teleports the player there and switches their game mode back.  
**Note:** Currently only Creative and Survival game modes are supported. Players cannot start in Adventure or Spectator mode.

If the mod fails to find a suitable spawn position, it falls back to the world’s default spawn.
