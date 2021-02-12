# csc413-tankgame


| Student Information |                |
|:-------------------:|----------------|
|  Student Name       |   Janson Leong  |
|  Student Email      |   jleong6@mail.sfsu.edu
|

## src Folder Purpose 
src folder is to be used to store source code only.

## resources Folder Purpose 
resources folder is to be used to store the resources for your project only. This includes images, sounds, map text files, etc.

`The src and resources folders can be deleted if you want a different file structure`

## jar Folder Purpose 
The jar folder is to be used to store the built jar of your term-project.

`NO SOURCE CODE SHOULD BE IN THIS FOLDER. DOING SO WILL CAUSE POINTS TO BE DEDUCTED`

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: 8

## IDE used: Intellij IDEA 2020.2.2

## Steps to Import project into IDE:
Clone or pull the repo into IntelliJ. 
(I wasn't able to get a proper JAR working.)

## Steps to Build your Project:
Build and run Launcher.java in src/tankrotationexample/ .

## Steps to run your Project:
After running the program, it should launch a start menu state. Click "Start" to begin the game.

## Controls to play your Game:
This version of Tank Game is a 1v1 pvp battle until the last person standing.
The Forward command moves the tank forward, as long as they aren't rotating left or right.
The Backward command moves the tank backward, as long as they aren't rotating left or right.
The Rotate Left command rotates the tank left, also prohibiting the tank from moving forward or backward while it is pressed.
The Rotate Right command rotate the tank right, also prohibiting the tank from moving forward or backward while is it pressed.
The Shoot command shoots a missle forward until it collides with another object. If it hits an unbreakable wall, it will explode and disappear
 without a trace. If it hits a breakable wall, it will explode and destroy the wall along with it. If it hits another tank, it will deal damage.
Each tank has 3 lives, each with an hp bar. When one tank has exhausted all 3 lives, a winning message for the player will pop up, with a small delay
 before entering the end screen.
On the end screen, there is a restart button to restart the game, or an exit button to close the game.

|               | Player 1 | Player 2 |
|---------------|----------|----------|
|  Forward      |    W     |    UP    |
|  Backward     |    S     |   DOWN   |
|  Rotate left  |    A     |   LEFT   |
|  Rotate Right |    D     |   RIGHT  |
|  Shoot        |    Q     |  ENTER   |

<!-- you may add more controls if you need to. -->

## Credits:
INST. Anthony Souza
Slack Channel: SFSU Computer Science
