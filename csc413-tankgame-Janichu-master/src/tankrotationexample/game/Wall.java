package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

//Wall class that implements the background and unbreakable walls as they're both immovable
//breakable walls have their own subclass
public class Wall extends GameObject{

    private boolean isBG;
    private static BufferedImage backgroundImg;
    private static BufferedImage unBreakWallImg;

    public Wall(int x, int y, boolean isBG){
        this.x = x;
        this.y = y;
        this.isBG = isBG;
        //the walls given are size 32x32
        this.myObject = new Rectangle(x,y,32,32);
    }

    public Wall(int x, int y) {
        this.x = x;
        this. y = y;
        this.isBG = false;
        this.myObject = new Rectangle(x, y, 32, 32);
    }

    public static void setBackgroundImg(BufferedImage img){
        backgroundImg = img;
    }

    public static void setUnBreakWallImg(BufferedImage img){
        unBreakWallImg = img;
    }
    //no need to update backgrounds because they don't change
    @Override
    public void update() {

    }

    @Override
    public void drawImage(Graphics2D g) {
        if (isBG){
            g.drawImage(backgroundImg,x,y,null);
        }
        else{
            g.drawImage(unBreakWallImg,x,y,null);
        }
    }

    //backgrounds dont cause events with other objects
    @Override
    public void event() {

    }
}
