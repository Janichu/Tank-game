package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{

    //to avoid the owner of the bullet damaging itself
    private String owner;
    //speed of the bullet needs to be fast than the tank, or it won't hit unless player is bad
    private final int S= 5;
    //checks if the bullet hits anything, stop drawing it if it does
    boolean collision = false;
    //large explosion for tanks, small explosion for walls
    private boolean isLarge = true;
    private boolean finished = false;
    // set the buffered images to be static because they will always be the same
    private static BufferedImage bullet;
    private static BufferedImage smallExplosion;
    private static BufferedImage largeExplosion;

    //need a timer so that we can show explosions,
    //otherwise bullet will be removed after collision, but before explosion.
    private int smallTimer = 0;

    //constructor to set position, direction to move in
    Bullet (int x, int y, int angle){
        this.x = x;
        this.y = y;
        this.vx = (int) Math.round(S * Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(S * Math.sin(Math.toRadians(angle)));
        this.angle = angle;
        this.myObject = new Rectangle(x, y, bullet.getWidth(), bullet.getHeight());
    }

    void setOwner(String owner){
        this.owner = owner;
    }

    String getOwner(){
        return this.owner;
    }

    static void setBulletImg(BufferedImage img){
        bullet = img;
    }
    static void setSmallExplosionImg(BufferedImage img){
        smallExplosion = img;
    }

    static void setLargeExplosionImg(BufferedImage img){
        largeExplosion = img;
    }

    //this method is used to tell the TRE that it's fine to delete the object
    boolean isFinished(){
        return this.finished;
    }

    //this method is needed because the program runs 3 more iterations in order to allow the explosion to show
    //without this, the tank will take damage 3 more times compared to just one hit.
    boolean isCollided(){
        return collision;
    }

    void setExplosion(boolean explosion){
        this.isLarge = explosion;
    }
    @Override
    public void update() {
        //checks if it collides
        //set a timer here for when the bullet collides so we have time to show the explosion before the bullet is deleted.
        //otherwise the explosion will flicker for a split second before disappearing.
        if (collision){
            smallTimer++;
        }
        //otherwise update it
        else {
            this.x += vx;
            this.y += vy;
            this.checkBorder();
        }

        this.myObject.setLocation(x,y);
    }

    @Override
    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), bullet.getWidth() / 2.0, bullet.getHeight() / 2.0);
        //if the bullet hits a tank, large explosion
        if (collision && isLarge){
            g.drawImage(largeExplosion,rotation,null);
            if (smallTimer >= 5)
                finished = true;
        }
        //if the bullet hits a breakable wall, small explosion
        else if (collision && !isLarge){
            g.drawImage(smallExplosion,rotation,null);
            if (smallTimer >= 5)
                finished = true;
        }
        else g.drawImage(bullet,rotation,null);
    }

    @Override
    public void event() {
        //the only thing a bullet does is collide and explode
        collision = true;
    }

    //checks if the bullet reaches out of bounds, if it does, it stops and disappears without exploding
    private void checkBorder(){
        if (x < 32) {
            this.finished = true;
        }
        if (x >= GameConstants.WORLD_SCREEN_WIDTH - 56) {
            this.finished = true;
        }
        if (y < 32) {
            this.finished = true;
        }
        if (y >= GameConstants.WORLD_SCREEN_HEIGHT - 56) {
            this.finished = true;
        }
    }
}
