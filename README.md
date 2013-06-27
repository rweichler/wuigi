# Wuigi #

## My APCS final project ##

It's a platformer. Mario clone. Except that it has a level editor where you can save and load levels. I had a version that also had networking but it's on one of my old hard drives somewhere and I'm too lazy to find it. Also, the initial commit is the only commit I have, as when I made this I didn't know what version control was. Sorry.

More details can be found in PRESENTATION.ppt.

To play:

    cd src/
    javac *.java
    java Wuigi

Known issues:

* There's a warning when compiling in GameScreen.
* The file loader sometimes freezes when entering directories with certain types of files in them (I think it was that they had a space or something, but it's been years since I've touched this, so I forgot. It's pretty easy to fix).
* I had no concept of data structures or proper game programming when I made this, so it tends to get kind of choppy when more objects are added to the world.

Other things you probably don't care about:

* I made this in high school. I started on it freshman year, the old version had networking and was not very well-made. The newer revision I made my junior year as a final project, but I had to take out networking due to time restraints. I don't know where the old version is. I might find it later and put it in another repository.
