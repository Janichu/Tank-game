package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall{

    //hp of the wall
    private int hp = 25;
    private boolean isDestroyed = false;
    private static BufferedImage breakWallImg;

    public BreakableWall(int x, int y) {
        super(x, y);
        this.myObject = new Rectangle(x,y,32,32);
    }

    public static void setBreakWallImg(BufferedImage img){
        breakWallImg = img;
    }
    private void removeHP(int hp){
        this.hp = 0;
        isDestroyed = true;
    }

    boolean isDestroyed(){
        return this.isDestroyed;
    }

    int getHP(){
        return this.hp;
    }

    //no need to implement update because the wall image doesn't need to change, only when it is destroyed
    public void update(){
    }

    public void drawImage(Graphics2D g){
        if (!isDestroyed){
            g.drawImage(breakWallImg,x,y,null);
        }
    }

    public void event(){
        removeHP(25);
    }
}
