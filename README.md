
  

# CS319 - Walls and Warriors


#### Group 1D - crack

  

## Group Members

  

Ahmet Malal (21502894)

  

  

Samet Demir (21502139)

  

  

Ibrahim Mammadov (21603109)

  

  

Huseyn Allahyarov (21503572)

  

  

Mahammad Shirinov (21603176)

  

  

## Description of the Game

  

Walls & Warriors is a board game played with warrior figures and walls placed under specific rules. The goal of this game place the four walls on the game board so that all the blue knights are inside the enclosure and all the red knights are on the outside to defend the castle to be left inside the walls. There is always only one correct solution.

  

  

## A Brief Description of Our Implementation

  

This is a desktop implementation of the Walls and Warriors game using Java in. It will contain all the functionalities of the original game, and some additional features will be added to increase enjoyment and difficulty, as described below:

  

  

## What's new

  

* Varied size of the board and shapes of walls

  

* Some types of warriors can walk in a specified path

  

* On harder levels, red soldiers are able to damage/break walls immediately near them

  

* Immobile objects on the board that limit the placement of the walls, such as rocks or forests that should be either entirely inside or outside the walls

  

* Lakes,red and blue ships on the board

  

* A limited number of chain objects to be used to provide protection of the castle from attacks from lakes

  

  

# Installation

## Windows:

  

* Open a **Command Prompt** in the main folder.

  

	+ You can do it by `Shift + Right Click` -> Open **PowerShell** window here.

  

	+ Or you can do it by `Windows + R` -> Search for **cmd**, then change the directory to the main folder you are in with **cd** command.

  

		\*Main folder is the extracted folder **cs319-Walls-and-Warriors-master**

  

* Run **`mkdir bin`**

  

* Run **`javac -d bin src/\*.java`**

  

* Run **`jar cvfm Walls^&Warriors.jar Manifest.txt -C bin .`**

  

  

If **javac** or **jar** commands do not work for you or return an error message as follows:

  

	'jar' is not recognized as an internal or external command, operable program or batch file.

	'javac' is not recognized as an internal or external command, operable program or batch file.

  

* Open a **Command Prompt**

  

* Change the directory to your boot disk (for ex. C:/) with the **`cd`** command. To go back in the path, use **`cd ..`** command.

  

* Run **`dir jar.exe \s`**

  

* Copy the file path that is as follows:

  

+ **[Disk]:\Program Files\Java\jdkX.X.X_X\bin**

  

	(*for ex.* C:\Program Files\Java\jdk1.8.0_191\bin)

  

* Run the commands as follows:

  

+ Run **`“FilePath/javac.exe” -d bin src/\*.java`**

  

	(*for ex.* `“C:\Program Files\Java\jdk1.8.0_191\bin\javac.exe” -d bin src/*.java`)

  

+ Run **`“FilePath/jar.exe” cvfm Walls^&Warriors.jar Manifest.txt -C bin .`**

  

	(*for ex.* `“C:\Program Files\Java\jdk1.8.0_191\bin\jar.exe” cvfm Walls^&Warriors.jar Manifest.txt -C bin .`)

  

## Mac & Linux:

  

* Open a terminal in the main folder.

  

	+ You can do it by `Command + Space` and search for terminal and press enter and then change the directory to the main folder with **`cd`** command.

  

		\*Main folder is the extracted folder **cs319-Walls-and-Warriors-master**

  

	+ In some distributions of Linux, you can right click in the folder and choose **Open in Terminal** option.

  

* Run **`mkdir bin`**

  

* Run **`javac -d bin src/\*.java`**

  

* Run **`jar cvfm Walls\&Warriors.jar Manifest.txt -C bin .`**

  

  

### References

[Game Rules](https://www.youtube.com/watch?v=kXX4OP38hYU)

[Trailer](https://www.youtube.com/watch?v=g3x0b1MU4yY)
