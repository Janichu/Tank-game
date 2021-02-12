package tankrotationexample.game;
//create a game object class so it can be used for games other than the tank game
import java.awt.*;

//possibly split between movable and nonmovable objects later on
public abstract class GameObject {

    // all objects have an x,y coordinate
    int x;
    int y;

    //movable objects have a direction and speed
    int vx;
    int vy;
    int angle;

    Rectangle myObject;

    void setX(int x){
        this.x = x;
    }

    void setY(int y){
        this.y = y;
    }

    void setVX(int vx){
        this.vx = vx;
    }

    void setVY(int vy){
        this.vy = vy;
    }

    void setAngle(int angle){
        this.angle = angle;
    }

    int getX(){
        return x;
    }

    int getY(){
        return y;
    }

    int getVX(){
        return vx;
    }

    int getVY(){
        return vy;
    }

    int getAngel(){
        return angle;
    }

    //used to update moveable objects
    public abstract void update();

    //used to draw every object in a game
    //in this case, tanks, bullets, walls are all objects
    public abstract void drawImage(Graphics2D g);

    //when an object interacts with another object
    //i.e. a bullet hits a tank, bullets hits a breakable wall
    //object may or may not need it.
    public abstract void event();
}

