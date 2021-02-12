package tankrotationexample.game;

import java.awt.*;
import java.util.ArrayList;
public class EventManager {
    ArrayList<GameObject> EventHandle(ArrayList<GameObject> gameObj){
        for (int i = 0; i < gameObj.size(); i++){
            if (gameObj.size() == 1) return gameObj;
            for (int j = 1; j < gameObj.size(); j++){
                GameObject obj1 = gameObj.get(i);
                GameObject obj2 = gameObj.get(j);

                //checks if obj1 is a tank, and obj2 is a bullet. Also checks if the bullet has collided already,
                //meaning it has already activated its event. Afterwards, ensures that the bullet is not owned by the tank
                //so it doesn't deal damage to itself.
                if (obj1 instanceof Tank && obj2 instanceof Bullet && !((Bullet) obj2).isCollided()
                        && !((Tank) obj1).getName().equals(((Bullet) obj2).getOwner())){
                    //checks if the rectangle objects are overlapping. If they overlap,
                    //they trigger events, otherwise nothing happens.
                    if (obj1.myObject.intersects(obj2.myObject)) {
                        obj1.event();
                        obj2.event();
                    }
                }
                //same as before, but obj1 and obj2 are switched
                if (obj1 instanceof Bullet && obj2 instanceof Tank && !((Bullet) obj1).isCollided()
                        && !((Tank) obj2).getName().equals(((Bullet) obj1).getOwner())){
                    if (obj1.myObject.intersects(obj2.myObject)){
                        obj1.event();
                        obj2.event();
                    }
                }
                //if a bullet hits a breakable wall
                //Bullet is always obj2 because all walls are already initialized into the array list.
                if (obj2 instanceof Bullet && obj1 instanceof BreakableWall && !((Bullet) obj2).isCollided()){
                    if (obj1.myObject.intersects(obj2.myObject)){
                        obj1.event();
                        obj2.event();
                    }
                }
                //if a bullet hits an unbreakable wall, it explodes but doesn't do anything
                //only checks wall because all the background images are in a separate arraylist
                //again, walls are all added into the arraylist when the game is initialized so it will be first
                if (obj1 instanceof Wall && obj2 instanceof Bullet && !((Bullet) obj2).isCollided()){
                    if (obj1.myObject.intersects(obj2.myObject)){
                        obj2.event();
                    }
                }
                //a tank must shoot a bullet to destroy a wall,not ram it otherwise it will just get stuck and stop moving
                if ((obj1 instanceof Wall || obj1 instanceof BreakableWall) && obj2 instanceof Tank){
                    Rectangle rect = ((Tank) obj2).checkMovement();
                    if (obj1.myObject.intersects(rect)){
                            ((Tank) obj2).setIsStuck(true);
                    }
                }
            }
        }

        return gameObj;
    }
}
