package Engine;

/**
 * Created by Maashes on 3/30/2016.
 */
import GameObject.*;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class Engine
{
    private int screenWidth;
    private int screenHeight;
    private ArrayList<GameObject> _gameObjectCollection;
    private GameObject ground;
    private Player player;
    private PApplet sketchParent;

    private final float gravityConstant = .5f;

    public Engine(int scrWidth, int scrHeight, PApplet parent)
    {
        screenWidth = scrWidth;
        screenHeight = scrHeight;
        _gameObjectCollection = new ArrayList(1);
        sketchParent = parent;
    }

    public Player CretePlayer(float x, float y, float objectWidth, float objectHeight)
    {
        player = new Player(x,y, objectWidth, objectHeight, this.sketchParent);
        player.SetIsPlayer(true);
        return player;
    }

    public void SetGroundLevel()
    {
        ground = new GameObject(0,screenHeight-15, screenWidth-1, 15, this.sketchParent);
        ground.SetIsGround(true);
        _gameObjectCollection.add(ground);
    }

    public void GeneratePlatforms()
    {
        GameObject p = new GameObject(200, ground.GetMinY() - 105, 50, 15, this.sketchParent);
        p.SetIsGround(true);
        _gameObjectCollection.add(p);
    }

    public void Update()
    {
        UpdatePlayer();

        for(GameObject g : _gameObjectCollection)
        {
            g.Update();
        }
    }

    private void UpdatePlayer()
    {
        //check to see if player has any collisions
        //if we have any, resolve them and adjust player location and velocity
        //otherwise if the player is in the air apply gravity
        //if the player is on the ground do nothing
        if(!player.GetIsOnGround())
        {
            if(player.GetVelocity().y < 10)
            {
                player.GetVelocity().y+=gravityConstant;
            }
        }
        else
        {
            player.SetIsJumping(false);
            player.GetVelocity().y = 0;
        }
        player.Update();
        player.SetIsOnGround(CheckOnGround(player));
    }

    public void Display()
    {
        player.Display();

        for(GameObject g : _gameObjectCollection)
        {
            g.Display();
        }
    }

    public boolean CheckCollision(GameObject obj1, GameObject obj2)
    {
        if(obj1.GetMaxX() < obj2.GetMinX() || obj1.GetMinX() > obj2.GetMaxX()) return false;

        if(obj1.GetMaxY() < obj2.GetMinY() || obj1.GetMaxY() > obj2.GetMaxY()) return false;

        return true;
    }

    public CollisionResult GetCollisionResult(GameObject objA, GameObject objB)
    {
        CollisionResult result = new CollisionResult();
        result.ObjectA = objA;
        result.ObjectB = objB;

        //find intersecting rectangle
        float xLeft = Math.max(objA.GetMinX(), objB.GetMinX());
        float yTop = Math.max(objA.GetMinY(), objB.GetMinY());
        float xRight = Math.min(objA.GetMaxX(), objB.GetMaxX());
        float yBottom = Math.min(objA.GetMaxY(), objB.GetMaxY());

        //overlap on x axis is width of overlapping rectangle
        float xOverlap = (xRight-xLeft);
        //overlap on y axis is height of overlapping rectangle
        float yOverlap = (yBottom-yTop);

        //Get vector from A to B
        PVector objALocation = new PVector(objA.GetLocation().x, objA.GetLocation().y);
        PVector objBLocation = new PVector(objB.GetLocation().x, objB.GetLocation().y);
        PVector locationVector = objBLocation.sub(objALocation);

        //if we have some overlapping distance
        if(xOverlap > 0 || yOverlap > 0)
        {
            //we want to choose overlap that is smallest
            if(xOverlap < yOverlap)
            {
                if(locationVector.x < 0)
                {
                    result.CollisionNormal = new PVector(-1, 0);
                    result.Direction = CollisionDirection.FROMLEFT;
                }
                else
                {
                    result.CollisionNormal = new PVector(1,0);
                    result.Direction = CollisionDirection.FROMRIGHT;
                }
                result.PenetrationDepth = xOverlap;
                return result;
            }
            else
            {
                if(locationVector.y < 0)
                {
                    result.CollisionNormal = new PVector(0, -1);
                    result.Direction = CollisionDirection.FROMBELOW;
                }
                else
                {
                    result.CollisionNormal = new PVector(0,1);
                    result.Direction = CollisionDirection.FROMABOVE;
                }
                result.PenetrationDepth = yOverlap;
                return result;
            }
        }

        return new CollisionResult();
    }

    public boolean CheckOnGround(GameObject obj)
    {
        for(GameObject object : _gameObjectCollection)
        {
            if(object.GetIsGround())
            {
                boolean objectIsOnGround = CheckOnPlatform(obj, object);

                if(objectIsOnGround)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean CheckOnPlatform(GameObject obj, GameObject groundObj)
    {
        //make sure we are colliding with the ground in some way
        if(CheckCollision(obj, groundObj))
        {
            CollisionResult result = GetCollisionResult(obj, groundObj);

            //if we are, make sure we are colliding from above the ground object
            if(result.Direction.equals(CollisionDirection.FROMABOVE))
            {
                return true;
            }
            else
            {
                //we collided with the platform from a different direction so we cant land on it
            }

        }
        return false;
    }

    public void ResolveCollision(GameObject objA, GameObject objB)
    {

    }
}
