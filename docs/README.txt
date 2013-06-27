HEADER:
Wuigi by Reed Weichler, completed May 30, 2011
________________
DESCRIPTION:
Like my last program, this program does not solve any problems. it would probably only create them.

Yes, it's a game. It's a mario clone, except with a couple of twists.
For those who don't know what mario is, you're pretty much this little short dude that jumps around on the screen trying to get from point A to point B without getting touched by any enemies. If he gets touched by an enemy, he dies and has to start over. Throughout the level there are various platforms and items that he can use to help himself get to the point B without any harm.

Now, in my game, it's a little different. Instead of playing the game, you CREATE the game. There are two modes to it, Level Editor and Singleplayer. In the Level Editor you can create a level and save it to a file to send to a  friend to play in Singleplayer. Or you could play it yourself if you don't have any friends. This program makes it easy for people who like mario to create their own levels. This program is awesome because the content never ends.

Did I mention that you can have a sniper rifle?
________________
INSTRUCTIONS:
In the main menu, it's pretty straight forward. There are 2 huge buttons that say Level Editor and SinglePlayer.  Click on one to start. When you click on Level Editor you have the option of making a new level or opening an existing one.

In the level editor, there is nothing. Just plains. In order to craft your level you have to spawn stuff. You can press SHIFT or Q in order to open the spawn menu. In the spawn menu you can click on various enemies, blocks, powerups or game mechanics to add to your level. Once you've completed your level, just hit ESCAPE and hit SAVE LEVEL. There should be a little window that pops up that lets you save your file to a *.wuigi file.

To play the level, just hit singleplayer instead of level editor. You'll get a little window that prompts you to open a file. Just click any file that has .wuigi at the end of it and you can play through the level. Use W,A,S,D or the arrow keys to move (or jump) and use your mouse to fire the sniper rifle if you have one.
_______________
CLASS LIST:
AePlayWave: plays wav files
AWPBullet: bullet fired from the sniper rifle
Backdrop: drawn behind all the action in the game (clouds/ground)
FileOpener: handles File i/o
GameScreen: superclass to the actual game
GroundHole: hole in the ground that can be fell through
Hero: the player
LevelEditor: the screen that lets you edit a level
MainScreen: the screen that's displayed when you open the .jar
PauseScreen: displayed when you hit ESC in game
Room: contains all enemies, blocks, items, and non-player Things and handles them
Screen: superclass to all screens. can handle key presses, mouse events and can draw to the screen
ScreenController: contained in every screen. controls the ScreenManager
ScreenManager: contains all of the screens and switches/initializes between each of them.
Serializer: converts classes to Strings so they can be written to file and created from a  file
SinglePlayer: the screen that lets you play through a level
SpawnScreen: the spawn menu
Sprite: makes images easily manipulable
TAWP: the sniper rifle
TBGBlock: a block that isn't solid, can be walked through
TBlock: a block that is solid, some kinds can be hit from the bottom for items
TBound: superclass to THorizontalBound and TVerticalBound
TCape: the cape that lets mario fly
TColoredBlock: a block that is represented by a solid color
TEnemy: superclass to all enemies (goombas, koopas, pirhanas, etc)
TextButton: helper class to display and handle clicks on strings drawn to the JPanel
TGoal: the goal mario has to touch to win
TGoomba: the little brown dude you can stomp on to kill
TGridded: a game object that is snapped to grid, superclass to all instances of TBlock and TPipe
Thing: superclass to all game objects including the player
THorizontalBound: an infinitely tall structure that cannot be walked through
TItem: item that can be equipped by the player
TKoopa: little green shell dude that can be stomped on
TLinker: links Things together (specifically Pipes)
TMetalCap: item that makes mario metallic
TPipe: structure that contains pirhanas and, when linked with another TPipe, can teleport the player
TPirhana : enemy that's found in TPipes
TRemover: tool that removes stuff in the Level Editor
TSpawn: the initial spawn in singleplayer
TStar: item that makes mario temporarily invincible
TTool: superclass to all level editor tools
TVerticalBound: infinitely wide structure that can't be walked through
TVisibleTool: a tool that is kept in the level editor after it is created(like a spawn or a goal, unlike a remover)
Wuigi: the JPanel, contains everything and stores constants
________________
RESPONSIBILITY LIST:
Reed Weichler: everything except for AePlayWave which was stolen from EasySound