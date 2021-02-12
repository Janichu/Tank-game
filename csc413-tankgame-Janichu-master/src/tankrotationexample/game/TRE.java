/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;


import tankrotationexample.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import static javax.imageio.ImageIO.read;
import static tankrotationexample.GameConstants.*;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private BufferedImage world;
    private BufferedImage p1Cam;
    private BufferedImage p2Cam;
    private Tank t1;
    private Tank t2;
    private int tank1Lives = 3;
    private int tank2Lives = 3;
    private boolean p1Wins = false;
    private boolean p2Wins = false;
    private long tick = 0;
    private Launcher lf;

    //create an event manager to manage all the objects in the array list
    //every iteration of updating will check for if an event happens
    //and will respond accordingly depending on which objects are interacting.
    private EventManager EM = new EventManager();

    public TRE(Launcher lf){
        this.lf = lf;
    }

    //have an arraylist to store all the game objects and update them
    private final ArrayList<GameObject> gameObj = new ArrayList<>();

    //function so the tank class can add bullets to the object list
    void addGameObjs(GameObject obj){
        this.gameObj.add(obj);
    }
    //a different list for nonchangeable objects like the background.
    //no point in having them all inside the gameObj list and iterating
    //through them because they won't be changed.
    private final ArrayList<GameObject> noChangeObj = new ArrayList<>();

    @Override
    public void run(){
       try {
           this.resetGame();
           while (true) {
               this.repaint();   // redraw game
               for (int i = 0; i < gameObj.size(); i++){
                   //checks if the bullet has hit anything yet
                   //if it has, remove it, otherwise update the bullet
                   if (gameObj.get(i) instanceof Bullet){
                       if (((Bullet) gameObj.get(i)).isFinished()){
                            gameObj.remove(i);
                            i--;
                       }
                       else gameObj.get(i).update();
                   }
                   //checks the tanks to see whether they've taken damage, or have been destroyed
                   //if destroyed, respawn the tank in the original spawn point.
                   if (gameObj.get(i) instanceof Tank){
                       if (((Tank) gameObj.get(i)).getHP() <= 0){
                           if (((Tank) gameObj.get(i)).getName().equals("Tank1")){
                               if (tank1Lives > 1){
                                   tank1Lives--;
                                   ((Tank) gameObj.get(i)).recover(150);
                                   gameObj.get(i).setX(100);
                                   gameObj.get(i).setY(100);
                                   gameObj.get(i).setAngle(0);
                               }
                               //if tank1 only has 1 left and hp is 0, it means they've lost
                               else {
                                   tank1Lives = 0;
                                   p2Wins = true;
                                   gameObj.remove(gameObj.get(i));
                                   tick = System.currentTimeMillis();
                                   break;
                               }
                           }
                           //object is a tank and is 0 hp, but not tank 1, so it must be tank 2
                           else {
                               if (tank2Lives > 1){
                                   tank2Lives--;
                                   ((Tank) gameObj.get(i)).recover(150);
                                   gameObj.get(i).setX(WORLD_SCREEN_WIDTH - 100);
                                   gameObj.get(i).setY(WORLD_SCREEN_HEIGHT - 100);
                                   gameObj.get(i).setAngle(180);
                               }
                               else{
                                   tank2Lives = 0;
                                   p1Wins= true;
                                   gameObj.remove(gameObj.get(i));
                                   tick = System.currentTimeMillis();
                                   break;
                               }
                           }
                       }
                   }
                   //if the object is a breakable wall and it's been hit, remove it from the object list.
                   if (gameObj.get(i) instanceof BreakableWall && ((BreakableWall) gameObj.get(i)).isDestroyed()){
                       gameObj.remove(i);
                   }
               }
               //call the event handler to check if any events between two objects has occured yet.
               this.EM.EventHandle(gameObj);
               //update the tanks
               t1.update();
               t2.update();

               Thread.sleep(1000/144); //sleep for a few milliseconds

           }
       } catch (InterruptedException ignored) {
           System.out.println(ignored);
       }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.t1.setX(100);
        this.t1.setY(100);
        this.t2.setX(WORLD_SCREEN_WIDTH - 150);
        this.t2.setY(WORLD_SCREEN_HEIGHT - 150);
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(WORLD_SCREEN_WIDTH,
                                       WORLD_SCREEN_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);

        BufferedImage t1Img = null, t2Img = null, bulletImg, smallExpImg, largeExpImg, backgroundImg,
                breakableWallImg, unbreakableWallImg;
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            t1Img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
            t2Img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank2.png")));

            bulletImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Rocket.gif")));
            Bullet.setBulletImg(bulletImg);

            smallExpImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Explosion_small.gif")));
            Bullet.setSmallExplosionImg(smallExpImg);

            largeExpImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Explosion_large.gif")));
            Bullet.setLargeExplosionImg(largeExpImg);

            backgroundImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp")));
            Wall.setBackgroundImg(backgroundImg);

            breakableWallImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall1.gif")));
            BreakableWall.setBreakWallImg(breakableWallImg);

            unbreakableWallImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall2.gif")));
            Wall.setUnBreakWallImg(unbreakableWallImg);

            //background = 0
            //breakable walls = 1
            //unbreakable walls = 2 (also the borders)
            //powerups = 3

            InputStreamReader isr = new InputStreamReader(TRE.class.getClassLoader().getResourceAsStream("map1"));
            BufferedReader mapReader = new BufferedReader(isr);

            String aRow = mapReader.readLine();
            if (aRow == null){
                throw new IOException("file is empty");
            }
            String[] mapInfo = aRow.split(" ");
            int numCols = Integer.parseInt(mapInfo[0]); //width = 1280, 1600/32 = 40
            int numRows = Integer.parseInt(mapInfo[1]); //height = 960, 1280/32 = 30

            for (int row = 0; row < numRows; row++){
                aRow = mapReader.readLine();
                mapInfo = aRow.split(" ");
                for (int col = 0; col < numCols; col++){
                    switch(mapInfo[col]){
                        //add a background object to the arraylist
                        case "0":
                            noChangeObj.add(new Wall(col * 32, row * 32, true));
                            break;
                        //adds a breakable wall object to the arraylist
                        case "1":
                            gameObj.add(new BreakableWall(col * 32, row * 32));
                            break;
                        //adds an unbreakable wall object to the arraylist
                        case"2":
                            gameObj.add(new Wall(col * 32, row * 32));
                            break;

                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        //spawns tank 1 on top left corner
        t1 = new Tank(100, 100, 0, 0, 0, t1Img);
        //spawns tank 2 on bottom right corner
        t2 = new Tank(WORLD_SCREEN_WIDTH - 150, WORLD_SCREEN_HEIGHT - 150, 0, 0, 180, t2Img);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Q);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        t1.setName("Tank1");
        t2.setName("Tank2");
        //adds
        gameObj.add(t1);
        gameObj.add(t2);
        //sets this object to be part of
        t1.setTRE(this);
        t2.setTRE(this);
        this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0,0, WORLD_SCREEN_WIDTH, WORLD_SCREEN_HEIGHT);
        for(int i = 0; i < noChangeObj.size(); i++) {
            noChangeObj.get(i).drawImage(buffer);
        }
        for(int i = 0; i < gameObj.size(); i++){
            gameObj.get(i).drawImage(buffer);
        }

        //center the split screens around the tanks, so we need to know its coordinates
        int t1X = t1.getX();
        int t1Y = t1.getY();
        int t2X = t2.getX();
        int t2Y = t2.getY();

        //if the players are on the edges of the screens, just show the edges of the map without moving
        //checking the bounds to make sure the subimage isn't outside of the world image
        if (t1X < GAME_SCREEN_WIDTH / 4){
            t1X = GAME_SCREEN_WIDTH / 4;
        }
        if (t2X < GAME_SCREEN_WIDTH / 4){
            t2X = GAME_SCREEN_WIDTH / 4;
        }
        if (t1X > WORLD_SCREEN_WIDTH - GAME_SCREEN_WIDTH / 4){
            t1X = WORLD_SCREEN_WIDTH - GAME_SCREEN_WIDTH / 4;
        }
        if (t2X > WORLD_SCREEN_WIDTH - GAME_SCREEN_WIDTH / 4){
            t2X = WORLD_SCREEN_WIDTH - GAME_SCREEN_WIDTH / 4;
        }
        if (t1Y < GAME_SCREEN_HEIGHT / 2){
            t1Y = GAME_SCREEN_HEIGHT / 2;
        }
        if (t2Y < GAME_SCREEN_HEIGHT / 2){
            t2Y = GAME_SCREEN_HEIGHT / 2;
        }
        if (t1Y > WORLD_SCREEN_HEIGHT - GAME_SCREEN_HEIGHT / 2){
            t1Y = WORLD_SCREEN_HEIGHT - GAME_SCREEN_HEIGHT / 2;
        }
        if (t2Y > WORLD_SCREEN_HEIGHT - GAME_SCREEN_HEIGHT / 2){
            t2Y = WORLD_SCREEN_HEIGHT - GAME_SCREEN_HEIGHT / 2;
        }
        //sets player 1's camera to top left and player 2's camera to  bottom right
        p1Cam = world.getSubimage(t1X - GAME_SCREEN_WIDTH / 4, t1Y - GAME_SCREEN_HEIGHT / 2, GAME_SCREEN_WIDTH /2, GAME_SCREEN_HEIGHT);
        p2Cam = world.getSubimage(t2X - GAME_SCREEN_WIDTH / 4, t2Y - GAME_SCREEN_HEIGHT / 2, GAME_SCREEN_WIDTH/2, GAME_SCREEN_HEIGHT);

        //p1 camera on the left side
        g2.drawImage(p1Cam,0,0,null);
        //p2 camera on the right side, +5 to draw a line in the middle
        g2.drawImage(p2Cam, GAME_SCREEN_WIDTH/2 + 5,0,null);
        //the world image is the minimap, set in the middle bottom so isn't beneficial to either player
        g2.drawImage(world,2 *GAME_SCREEN_WIDTH / 5,  2 * GAME_SCREEN_HEIGHT / 3, GAME_SCREEN_WIDTH / 5,GAME_SCREEN_HEIGHT / 3, null);

        //showing the number of lives
        //set the color of the font
        g2.setColor(Color.GREEN);
        //set the font of the string
        g2.setFont(new Font("Serif",Font.BOLD,24));
        g2.drawString("Player 1's lives: " + this.tank1Lives, 10, 28);
        g2.drawString("Player 2's lives: " + this.tank2Lives, GAME_SCREEN_WIDTH/2 + 10, 28);

        //amount of hp left in numerical form
//        g2.drawString("Player 1's HP: " + this.t1.getHP(), 10, 58);
//        g2.drawString("Player 2's HP: " + this.t2.getHP(), GAME_SCREEN_WIDTH/2 + 10, 58);
        //hp bar
        g2.fillRect(25, 40, 2 * t1.getHP(), 20);
        g2.fillRect(GAME_SCREEN_WIDTH / 2 + 25, 40, 2 * t2.getHP(), 20);

        if (p1Wins || p2Wins){
            if (p1Wins){
                g2.drawString("Player 1 wins!", 305, GAME_SCREEN_HEIGHT/2);
                if (System.currentTimeMillis() - tick > 2000){
                    this.lf.setFrame("end");
                }
            }
            if (p2Wins){
                g2.drawString("Player 2 wins!", 305, GAME_SCREEN_HEIGHT/2);
                if (System.currentTimeMillis() - tick > 2000){
                    this.lf.setFrame("end");
                }
            }
        }
    }
}
