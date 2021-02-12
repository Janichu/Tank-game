package tankrotationexample.game;



import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{

    //variables for position and velocity are inherited
    private final int R = 3;
    private final float ROTATIONSPEED = 3;
    private int health = 150;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private long lastFired = 0;
    //makes sure a tank doesn't get hit by its own bullet
    private String name;
    private boolean isStuck = false;

    //allows the tanks to add bullets to the list of game objects and update them
    private TRE tre;

    void setTRE(TRE tre){
        this.tre = tre;
    }
    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        myObject = new Rectangle(x, y, img.getWidth(), img.getHeight());

    }

    void setX(int x){ this.x = x; }

    void setY(int y) { this. y = y;}

    void setName(String name){
        this.name = name;
    }

    String getName(){
        return this.name;
    }

    int getHP(){
        return this.health;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void shootPressed(){
        this.ShootPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unShootPressed(){
        this.ShootPressed = false;
    }

    //constantly updates the tank based on input
    public void update() {


        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        //set a cooldown on shooting, otherwise the player will just hold the button.
        if (this.ShootPressed && (System.currentTimeMillis() - lastFired > 500)){
            this.shootBullet(x, y, vx, vy, angle, tre);
            lastFired = System.currentTimeMillis();
        }
        this.isStuck = false;
        this.myObject.setLocation(x,y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        if (!isStuck) {
            x -= vx;
            y -= vy;
        }
        checkBorder();

    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        if (!isStuck){
            x += vx;
            y += vy;
        }
        checkBorder();
    }

    Rectangle checkMovement() {
        this.myObject.setLocation(x + vx,y + vy);
        return myObject;
    }

    private void checkBorder() {
        if (x < 32) {
            x = 32;
        }
        if (x >= GameConstants.WORLD_SCREEN_WIDTH - 82) {
            x = GameConstants.WORLD_SCREEN_WIDTH - 82;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= GameConstants.WORLD_SCREEN_HEIGHT - 82) {
            y = GameConstants.WORLD_SCREEN_HEIGHT - 82;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, rotation, null);
        //allows the players to see the hitboxes
        g.setColor(Color.RED);
        // g2d.rotate(Math.toRadians(angle),bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g.drawRect(x, y, img.getWidth(), img.getHeight());
    }

    @Override
    public void event() {
        this.removeHP(25);
    }

    private void removeHP(int damage){
        if (health - damage < 0) health = 0;
        else health -= damage;
    }

    void recover(int heal){
        if (health + heal > 200) health = 200;
        else health += heal;
    }

    private void shootBullet(int x, int y,int vx, int vy, int angle, TRE tre){
        Bullet blt = new Bullet(x,y,angle);
        blt.setOwner(this.name);
        tre.addGameObjs(blt);
    }

    void setIsStuck(boolean stuck){
        this.isStuck = stuck;
    }

    boolean getIsStuck(){
        return isStuck;
    }

}
