



import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Hero extends Thing{
	
	
	//FIELDS
	
		private static final AePlayWave JUMP = new AePlayWave("sound/mario_jump.wav");
		private static final AePlayWave AWP = new AePlayWave("sound/awp1.wav");
		private static final String MARIO_SPRITE_PATH = "images/sprites/mario_new/";
		
		
		private boolean jumpDown;
		
		private boolean dead;
		private double deadTime;
		
		/**
		 * draw images
		 */
		public final Sprite[] IMAGE = {
			new Sprite(MARIO_SPRITE_PATH+"stand.gif"),
			new Sprite(MARIO_SPRITE_PATH+"jump.gif"),
			new Sprite(MARIO_SPRITE_PATH+"walk.gif"),
			new Sprite(MARIO_SPRITE_PATH+"stand.gif"),
			new Sprite(MARIO_SPRITE_PATH+"switch.gif"),
			new Sprite(MARIO_SPRITE_PATH+"dead.gif"),
			
		};
		
		private final Color[] IMAGE_COLORS = {
			new Color(248,48,80), //light
			new Color(216,0,40),
			new Color(176,0,0),//dark
		};
		
		private boolean movingUp;
		
		private boolean star;
		private double starTime;
		
		private boolean cape;
		private double capeTime;
		
		private boolean invulerable;
		private double invulerableTime;
		
		boolean metal;
		
		private int room = -1;
		private Point2D.Double telePos = new Point2D.Double();
		
		private byte move;
		private double xOffset;
		private double yOffset;
		private boolean jumping,crouched,piped,changeWorlds,infSpeed;
		
		private Point2D.Double deathPos;
		
		private double jumpAdd;
		private double lineToCross;
		
		private static final double
			X_OFFSET = 100,
			Y_OFFSET = Wuigi.screenHeight - 30,
			MAX_X_VEL = 5,
			X_VEL_PER_TICK = 0.1,
			JUMP_VEL = 3;
		
		public static final int
			WIDTH = 24,
			HEIGHT = 30;
		
		private int spriteColor = 0;
		
		private boolean won;
		private static TextButton ammo;
		
		private int numBullets;
		private long shotTime;
		
		private boolean facingRight,movingRight,movingLeft,rightFoot;
		
		private double lastX;
		
	
	//CONSTRUCTOR
		public Hero(){
			super(0,0,WIDTH,HEIGHT);
			deathPos = new Point2D.Double();
			metal = false;
			numBullets = 0;
			shotTime = 0;
			ammo = new TextButton("", TAWP.AMMO, Color.YELLOW);
		}
		
		public void init(){
			jumpDown = false;
			movingUp = false;
			
			dead = false;
			deadTime = 0;
			
			star = false;
			starTime = 0;
			
			won = false;
			
			cape = false;
			capeTime = 0;
			
			pos = new Point2D.Double();
			vel = new Point2D.Double();
			acc = new Point2D.Double();
			
			invulerable = false;
			invulerableTime = 0;
			xOffset = 0;
			yOffset = Y_OFFSET/8;
			move = 0;
			jumpAdd = 0;
			facingRight = true;
			lastX = 0;
			rightFoot = true;
			
		}
	
	
	//ACTION METHODS
		
		
		public void setSpriteColor(int c){setSpriteColor(c,false);}
		
		/**
		 * 
		 * @param override true if should kill even if invulerable, false if not
		 */
		public void kill(boolean override){
			if(!override){
				if(star || invulerable || piped)return;
			}
			deathPos.setLocation(pos);
			if(deathPos.y < 0){
				deathPos.y = 0;
			}
			if(metal){
				metal = false;
				startInvulnerable();
				invulerableTime = 3000/15;
				Point2D.Double v = new Point2D.Double(Math.random()*8 - 4, Math.random()*4 + 7);
				
				TMetalCap cap = new TMetalCap();
				cap.setPos(pos.x,pos.y);
				cap.kill(v);
				this.addSpawn(cap);
				return;
			}
			cape = false;
			dead = true;
			numBullets = 0;
			falling = false;
			acc = new Point2D.Double();
			vel = new Point2D.Double();
			deadTime = 25;
		}
		
		/**
		 * determines if in star mode
		 * @return true if in star mode
		 */
		public boolean isInStarMode(){
			return star;
		}
		
		public void kill(){
			kill(false);
		}
		
		public boolean enableGravity(){
			return true;
		}
		
		/**
		 * sets the respawn position
		 */
		public void setDeathPos(){
			setPos(deathPos.x,deathPos.y);
		}
		
		/**
		 * changes the sprite color
		 * @param color color that should be changed to <br/>&nbsp;&nbsp;1: green<br/>&nbsp;&nbsp;2:blue<br/>&nbsp;&nbsp;3:yellow<br/>&nbsp;&nbsp;4:purple<br/>&nbsp;&nbsp;5:gray
		 * @param star whether or not this is just for the starman sequence
		 */
		public void setSpriteColor(int color, boolean star){
			if(!star)
				spriteColor = color;
			Color[] replace = new Color[3];
			switch(color){
				case 0: //red
					for(int i = 0; i < IMAGE.length; i++)
						(IMAGE[i]).renew();
					return; //the image is already red so why go through all sorts of bullshit?
				//break;
				
				case 1: //green
					replace[0] = new Color(41,255,41);
					replace[1] = new Color(0,216,0);
					replace[2] = new Color(0,176,0);
				break;
				
				case 2: //blue
					replace[0] = new Color(41,171,255);
					replace[1] = new Color(0,140,216);
					replace[2] = new Color(0,100,176);
				break;
				
				case 3: //yellow
					replace[0] = new Color(252,255,41);
					replace[1] = new Color(213,216,0);
					replace[2] = new Color(174,176,0);
				break;
				
				case 4: //purple
					replace[0] = new Color(255,41,252);
					replace[1] = new Color(216,0,213);
					replace[2] = new Color(176,0,174);
				break;
				
				default: //gray
					replace[0] = new Color(250,250,250);
					replace[1] = new Color(170,170,170);
					replace[2] = new Color(150,150,150);
				break;
			}
			
			for(int i = 0; i < IMAGE.length; i++){
				Sprite limg = IMAGE[i];
				limg.renew();
				limg.replaceColors(IMAGE_COLORS, replace);
			}
		}
		/**
		 * starts the metal cap sequence
		 */
		public void startMetal(){
			metal = true;
		}
		
		/**
		 * starts the starman sequence
		 */
		public void startStar(){
			star = true;
			starTime = 0;
		}
		
		/**
		 * starts temporary invulerability
		 */
		public void startInvulnerable(){
			invulerable = true;
			invulerableTime = 0;
		}
		
		/**
		 * equips the cape so this can fly
		 */
		public void startCape(){
			cape = true;
			capeTime = 0;
		}
		
		/**
		 * gives the player an Arctic Warfare Magnum Sniper Rifle. Oh dear.
		 */
		public void giveAWP(){
			numBullets += 2;
			ammo.setText("" + numBullets);
		}
		/**
		 * shoots the AWP
		 */
		public void shootAWP(int x, int y){
			if(numBullets > 0){
				numBullets --;
				AWP.start();
				shotTime = System.currentTimeMillis();
				ammo.setText("" + numBullets);
				
				double awpx = pos.x + width/2;
				double awpy = pos.y + height/2;
				
				double mousex = x + this.pos.x - (Wuigi.screenWidth/2.0 + xOffset());
				double mousey = Wuigi.screenHeight + pos.y + 32 - (ScreenManager.mouse.y + yOffset());
				
				

				addSpawn(new AWPBullet(awpx,awpy,mousex,mousey));
			}
		}
		
		/**
		 * called when this wants to move through a pipe
		 * @param lineToCross the y coordinate this must pass before teleporting to its new position
		 * @param room room to move to
		 * @param telePos position to move to
		 */
		public void pipe(double lineToCross, int room, Point2D.Double telePos){
			if(crouched){
				new AePlayWave("sound/pipe.wav").start();
				crouched = false;
				piped = true;
				vel.y = -1;
				vel.x = 0;
				this.lineToCross = lineToCross;
				this.room = room;
				this.telePos.setLocation(telePos);
			}
		}
		/**
		 * sets this position to that of the new pipe it should move to
		 * @return room this should move to
		 */
		public int getRoomAndSetNewPosition(){
			this.setPos(telePos.x,telePos.y);
			vel.y = 1;
			vel.x = 0;
			piped = true;
			lineToCross = (int)(pos.y + height);
			return room;
		}
		/**
		 * determines if the player finished moving through a pipe and needs to change to the connected pipe
		 * @return true if finished moving through a pipe, false if not
		 */
		public boolean piped(){
			boolean p = changeWorlds;
			if(p){
				piped = false;
				setPos(pos.x, (double)lineToCross);
				vel.y = 0;
			}
			changeWorlds = false;
			return p;
		}
		/**
		 * determines if the player is moving through a pipe
		 * @return true if moving through a pipe
		 */
		public boolean piping(){
			return piped;
		}
		/**
		 * makes this crouch (for pipes)
		 * @param crouched
		 */
		public void crouch(boolean crouched){
			this.crouched = crouched;
		}
		/**
		 * makes this accelerate horizontally
		 * @param rightward true if should move right, false if should move left
		 */
		public void move(boolean rightward){
			if(rightward){
				move = 1;
			}else{
				move = -1;
			}
			movingRight = rightward;
			movingLeft = !rightward;
			facingRight = rightward;
		}
		/**
		 * makes this stop accelerating horizontally
		 * @param rightward true if the unpressed button is the right key, false if it is the left key
		 */
		public void stop(boolean rightward){
			if(move == -1 && !rightward || move == 1 && rightward)
				move = 0;
			if(rightward)
				movingRight = false;
			else
				movingLeft = false;
		}
		/**
		 * determines if the jump key is down or not (used when stomping on enemies)
		 * @return true if the jump button is down, false if not
		 */
		public boolean jumpDown(){
			return jumpDown;
		}

		/**
		 * makes this jump, makes the sound play by default
		 * @param pressed true if jump button is down
		 */
		public void jump(boolean pressed){
			jump(pressed,true);
		}
		/**
		 * makes this jump
		 * @param pressed true if jump button is down
		 * @param sound true if should play sound
		 */
		public void jump(boolean pressed, boolean sound){
			if(dead) return;
			if(pressed)
				jumpDown = true;
			else
				jumpDown = false;
			if(cape){
				if(pressed){
					movingUp = true;
					acc.y = Wuigi.GRAVITY;
					jumping = true;
				}else{
					movingUp = false;
					jumping = false;
					acc.y = 0;
				}
				return;
			}
			
			if(pressed){
				if(vel.y == 0 ){
					vel.y = JUMP_VEL + Wuigi.GRAVITY;
					jumping = true;
					jumpAdd = Wuigi.COMPLETE_JUMP_TIME;
					if(sound)
						JUMP.start();
				}
			}else{
				jumpAdd = -Wuigi.COMPLETE_JUMP_TIME;
			}
		}
		//double jolt = 
		private void jumpAdd(){
			if(vel.y == 0 || dead){
				jumpAdd = 0;
			}else{
				double add = 0.4*jumpAdd/Wuigi.COMPLETE_JUMP_TIME*Wuigi.time();
				vel.y += add;
			}
		}
		
		public void setPos(double x, double y){
			if(x != pos.x)
				xOffset -= pos.x -  x;
			if(y != pos.y)
				yOffset -= pos.y -  y;
			pos.setLocation(x,y);
		}
		
		public void setPos(Point2D.Double pos){
			setPos(pos.x,pos.y);
		}
		
		public boolean touching(Thing t){
			if(piped && vel.y < 0 || dead)
				return false;
			else
				return super.touching(t);
		}
		
		public void onTouch(Thing t){
			if(dead) return;
			if(star && t instanceof TEnemy ){
				new AePlayWave("sound/kick.wav").start();
				t.kill(new Point2D.Double(vel.x*2, Math.random()*16+3));
			}else if(t instanceof TGoal){
				won = true;
			}
		}
		/**
		 * determines if this hit a TGoal
		 * @return true if this hit the goal
		 */
		public boolean won(){
			boolean temp = won;
			won = false;
			return temp;
		}
		
	//CONSTANT METHODS
		public void draw(Graphics g, ImageObserver o){
			
			boolean transparent = invulerable && (invulerableTime < 3000/15 || System.currentTimeMillis() % 60 > 30);
			if(transparent)
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			int x = Wuigi.scaleW(Wuigi.screenWidth/2.0 + xOffset - 4);
			int y = Wuigi.scaleH(Wuigi.screenHeight - yOffset);
			if(cape){
				if(facingRight)
					g.drawImage(TCape.IMAGE.getBuffer(),x-5, y,Wuigi.scaleW(WIDTH + 8),Wuigi.scaleH(HEIGHT + 2), o);
				else
					g.drawImage(TCape.IMAGE.flipX(),x+5, y,Wuigi.scaleW(WIDTH + 8), Wuigi.scaleH(HEIGHT + 2), o);
			}
			
			drawAWP(g,x,y);
			
			g.drawImage(
				figureOutDrawImage(IMAGE),
				x,
				//(int)(Global.H-H-pos.y-Global.GROUND_LEVEL),
				y,
				Wuigi.scaleW(WIDTH + 8),
				Wuigi.scaleH(HEIGHT + 2),
				o
			);
			if(transparent)
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
			//g.setColor(Color.WHITE);
			//g.drawRect((int)(Wuigi.W/2 - X_OFFSET), (int)(Wuigi.H - Y_OFFSET), (int)X_OFFSET*2 + width, (int)(Y_OFFSET*2) - Wuigi.H);
		}
		
		private void drawAWP(Graphics g, int x, int y){
			if(System.currentTimeMillis() < shotTime + TAWP.SHOOT_TIME){
				//System.out.println(true);
				double multi = 1.0*(System.currentTimeMillis() - shotTime)/TAWP.SHOOT_TIME;
				g.setColor(new Color(255,255,255,255-(int)(multi*256)));
				g.fillRect(0,0,Wuigi.screenWidth,Wuigi.screenHeight);
			}
			if(numBullets <= 0) return;
			
			BufferedImage img = TAWP.IMAGE.getBuffer();
			AffineTransform xform = new AffineTransform();
			//g2d.setPaint(new TexturePaint(figureOutDrawImage(),
			//		new Rectangle2D.Float(0, 0, 32, 32)));
			//g2d.drawImage(figureOutDrawImage(),0,0,null);
			xform.setToIdentity();
			xform.translate(x-5, y);
			
			double diffY = y + height/2 - ScreenManager.mouse.y;
			double diffX = ScreenManager.mouse.x - x - width/2;
			
			double angle;
			
			if(diffY > 0 && diffX > 0){
				angle = Math.PI/2 - Math.atan(diffY/diffX);
			}else if(diffY == 0 && diffX > 0){
				angle = Math.PI/2;
			}else if(diffY < 0 && diffX > 0){
				angle = Math.PI/2 + Math.atan(-diffY/diffX);
			}else if(diffY < 0 && diffX == 0){
				angle = Math.PI;
			}else if(diffY < 0 && diffX < 0){
				angle = 3*Math.PI/2 - Math.atan(diffY/diffX);
			}else if(diffY == 0 && diffX < 0){
				angle = 3*Math.PI/2;
			}else if(diffY > 0 && diffX < 0){
				angle = 3*Math.PI/2 + Math.atan(-diffY/diffX);
			}else{
				angle = 0;
			}
			angle -= Math.PI/2;
			
			if(angle > Math.PI/2 && angle < 3*Math.PI/2){
				xform.scale(1, -1);
				xform.translate(0,-TAWP.IMAGE.getHeight());
				angle = -angle;
			}
			//xform.scale(/*width/img.getWidth(),height/img.getHeight()*/);
			xform.rotate(angle, 27, 13);

			((Graphics2D)g).drawImage(img,xform, null);
			ammo.setPos(x + 10, y -20);
			ammo.draw(g);
		}
		
		private BufferedImage figureOutDrawImage(Sprite[] imgs){
			
			Sprite img;
			if(dead){
				return imgs[5].getBuffer();
			}
			
			if(Math.abs((int)(vel.y*100)) > 0 && !piped){
				img = imgs[1];
			}else if( !(movingRight || movingLeft) && vel.x == 0 )
				img = imgs[0];
			else{
				if(movingRight && vel.x < 0 || movingLeft && vel.x > 0)
					img = imgs[4];
				else{
					if(rightFoot)
						img = imgs[3];
					else
						img = imgs[2];
				}
			}

			if(star && (starTime < 8000/15 || System.currentTimeMillis() % 60 > 30)){
				int	width = img.getBuffer().getWidth(),
					height = img.getBuffer().getHeight();
				Sprite limg = new Sprite(img.getBuffer());
				for(int x = 0; x < width; x++){
					for(int y = 0; y < height; y++){
						Color pixel = limg.getPixel(x,y);
						if(pixel.getAlpha() != 0){
							limg.setPixel(x,y,new Color(255 - pixel.getRed(), 255 - pixel.getGreen(), 255 - pixel.getBlue(), pixel.getAlpha()));
						}
					}
				}
				img = limg;
			}else if(metal){
				int	width = img.getBuffer().getWidth(),
					height = img.getBuffer().getHeight();
				Sprite limg = new Sprite(img.getBuffer());
				for(int x = 0; x < width; x++){
					for(int y = 0; y < height; y++){
						Color pixel = limg.getPixel(x,y);
						if(pixel.getAlpha() != 0){
							int gray = (pixel.getRed() + pixel.getBlue() + pixel.getGreen())/3;
							limg.setPixel(x,y,new Color(gray,gray,gray, pixel.getAlpha()));
						}
					}
				}
				img = limg;
			}
			
			if(facingRight){
				return img.flipX();
			}else{
				return img.getBuffer();
			}
		}
		
		public boolean dying(){
			return dead;
		}
		/**
		 * determines if the player died, and already went to the bottom of the screen after the little jump upward
		 * @return true if dead, false if not
		 */
		public boolean isDead(){
			return dead && yOffset < -300;
		}
		
		public void think(){
			if(dead && deadTime > 0){
				deadTime -= Wuigi.time();
				if(deadTime < 0){
					vel.x = 0;
					vel.y = 15;
				}
				return;
			}
			

			if(star){
				starTime += Wuigi.time();
				if(starTime > 10000/15){ //if you've been in starman mode for more than 10 seconds, stop
					star = false;
					starTime = 0;
					setSpriteColor(spriteColor,true);
				}else{
					setSpriteColor((int)(Math.random()*6),true);
				}
			}
			
			if(invulerable){
				invulerableTime += Wuigi.time();
				if(invulerableTime > 5000/15){
					invulerable = false;
					invulerableTime = 0;
				}
			}
			
			if(cape){
				capeTime += Wuigi.time();
				if(capeTime > 10000/15){
					cape = false;
					capeTime = 0;
				}
			}
			
			

			updatePosition();
			xOffset += pos.x - posLast.x;
			if(xOffset > X_OFFSET)
				xOffset = X_OFFSET;
			else if(xOffset < -X_OFFSET)
				xOffset = -X_OFFSET;
				
			yOffset += pos.y - posLast.y;
			if(!(dead || falling)){
				if(yOffset > Y_OFFSET)
					yOffset = Y_OFFSET;
				else if(yOffset < Y_OFFSET/8)
					yOffset = Y_OFFSET/8;
			}
			updateVelocity();
			//double v = vel.y;
			//if(v > 0 && vel.y < 0){
			//	System.out.println("position: " + pos.y);
			//}
			falling = false;
		}
		
		private void updateVelocity(){
			if(piped){
				return;
			}
			if(jumpAdd > 0){
				jumpAdd-= Wuigi.time();
				jumpAdd();
				acc.y = 0;
			}
			if(jumping){
				jumping = false;
			}else if(!dead){
				int tempMove = move;
				if(move == 0){
					if(vel.x > 0)
						tempMove = -1;
					else
						tempMove = 1;
				}
				double xadd;
				//stopping
				if(vel.x > 0 && move <= 0 || vel.x < 0 && move >= 0){
					xadd = tempMove*X_VEL_PER_TICK*2.0*Wuigi.time();
				//moving forward
				}else{
					xadd = tempMove*X_VEL_PER_TICK*Wuigi.time();
				}
				if(vel.y < 0 && !metal){
					vel.x += xadd/3;
				}else{
					vel.x += xadd;
				}
				if(move == 0 && tempMove*vel.x > 0){
					vel.x = 0;
				}
				if(!infSpeed){
					if(vel.x < -MAX_X_VEL)
						vel.x = -MAX_X_VEL;
					else if(vel.x > MAX_X_VEL)
						vel.x = MAX_X_VEL;
				}
			}
			
			if(vel.x < X_VEL_PER_TICK*Wuigi.time() && vel.x > -X_VEL_PER_TICK*Wuigi.time())
				vel.x = 0;
			
			vel.x += acc.x*Wuigi.time();
			vel.y += acc.y*Wuigi.time();
			
			//S//ystem.out.println(acc.y);
			if(falling && invulerable){
				falling = false;
			}
			if(pos.y > 0 && !(cape && movingUp) || falling){
				acc.y = -Wuigi.GRAVITY;
				if(cape){
					acc.y /= 4;
				}
			}else if(cape && movingUp && vel.y > 5){
				acc.y = 0;
				vel.y = 5;
			}
			else if(!jumping && !dead && !(cape && movingUp)){
				vel.y = 0;
				acc.y = 0;
			}
		}
		
		private void updatePosition(){
			updatePosLast();
			
			pos.setLocation(pos.x+vel.x*Wuigi.time(),pos.y+vel.y*Wuigi.time());

			if(!dead && pos.y+Wuigi.GROUND_LEVEL < 0){
				kill(true);
				//pos.y = pos.y + Wuigi.GROUND_LEVEL + 1;
				falling = false;
			}else if(pos.y < 0 && !dead && !piped && (!falling || invulerable)){
				setPos(pos.x,0);
				//yOffset = Wuigi.GROUND_LEVEL;
			}
			
			if(pos.x > lastX + 20 || pos.x < lastX - 20){
				rightFoot = !rightFoot;
				lastX = pos.x;
			}
			if(piped && pos.y + height< lineToCross && vel.y < 0) changeWorlds = true;
			if(piped && pos.y > lineToCross && vel.y > 0){
				vel.y = 0;
				pos.y = lineToCross;
				piped = false;
			}
			
		}
	
	//RETRIEVAL METHODS
		
		/**
		 * gets the x-offset (how many pixels the player is from the center of the screen)
		 */
		public double xOffset(){
			return xOffset;
		}
		/**
		 * gets the y-offset (how many pixels the player is from the center of the screen)
		 */
		public double yOffset(){
			return yOffset;
		}
		/**
		 * gets the x coordinate where the player is supposed to be drawn to the screen
		 */
		public double viewX(){
			return pos.x-xOffset+Wuigi.screenWidth/2.0;
		}
		
		/**
		 * gets the y coordinate of where the player is supposed to be drawn to the screen
		 */
		public double viewY(){
			return Wuigi.screenHeight - yOffset + pos.y + height;
		}
		
		public void bumpX(){
			vel.x = 0;
		}
	
}