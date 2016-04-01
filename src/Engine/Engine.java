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
        GameObject ground = new GameObject(0,screenHeight-15, screenWidth-1, 15, this.sketchParent);
        ground.SetIsGround(true);
        _gameObjectCollection.add(ground);
    }

    public void GeneratePlatforms()
    {
        GameObject p = new GameObject(200, 585 - 105, 50, 15, this.sketchParent);
        p.SetIsGround(true);
        _gameObjectCollection.add(p);

        GameObject p1 = new GameObject(400, 585 - 50, 20, 50, this.sketchParent);
        p1.SetIsGround(true);
        _gameObjectCollection.add(p1);
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
        player.Update();
        //check to see if player has any collisions
        ArrayList<CollisionResult> collisions = CheckPlayerCollisions();
        if(!collisions.isEmpty())
        {
            //if we have any, resolve them and adjust player location and velocity
            for(CollisionResult result : collisions)
            {
                ResolveCollision(result);
            }
        }

        if(player.GetVelocity().y < 10)
        {
            player.GetVelocity().y+=gravityConstant;
        }

        if(player.GetIsOnGround())
        {
            player.SetIsJumping(false);
        }

        player.SetIsOnGround(CheckOnGround(player));
    }

    private ArrayList<CollisionResult> CheckPlayerCollisions()
    {
        ArrayList<CollisionResult> results = new ArrayList<CollisionResult>();
        for(GameObject g : _gameObjectCollection)
        {
            if(CheckCollision(player, g))
            {
                results.add(GetCollisionResult(player,g));
            }
        }
        return results;
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

        if(obj1.GetMaxY() < obj2.GetMinY() || obj1.GetMinY() > obj2.GetMaxY()) return false;

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
        PVector objALocation = new PVector(objA.GetMinX(), objA.GetMinY());
        PVector objBLocation = new PVector(objB.GetMinX(), objB.GetMinY());
        PVector locationVector = new PVector((objBLocation.x - objALocation.x),(objBLocation.y - objALocation.y));

        //if we have some overlapping distance
        if(xOverlap > 0 || yOverlap > 0)
        {
            //we want to choose overlap that is smallest
            if(xOverlap < yOverlap)
            {
                if(locationVector.x > 0)
                {
                    result.Direction = CollisionDirection.FROMLEFT;
                }
                else
                {
                    result.Direction = CollisionDirection.FROMRIGHT;
                }
                result.PenetrationDepth = xOverlap;
                return result;
            }
            else
            {
                if(locationVector.y < 0)
                {
                    result.Direction = CollisionDirection.FROMBELOW;
                }
                else
                {
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
                boolean collidingWithGround = CheckCollision(obj, object);

                if(collidingWithGround)
                {
                    CollisionResult result = GetCollisionResult(obj, object);

                    if(result.Direction.equals(CollisionDirection.FROMABOVE))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void ResolveCollision(CollisionResult result)
    {
        switch (result.Direction)
        {
            case FROMABOVE:
                //if we are colliding from above
                //if both objects are not ground objects, we want to move them in opposite directions
                //each half of the total penetration distance. In this case, the -y direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().y += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().y = 0;
                    result.ObjectB.GetVelocity().y = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= result.PenetrationDepth;
                    result.ObjectA.GetVelocity().y = 0;
                }
                break;
            case FROMBELOW:
                //colliding from below is like coliding from above except we need to move things in the positive y direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().y += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().y = 0;
                    result.ObjectB.GetVelocity().y = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y += result.PenetrationDepth;
                    result.ObjectA.GetVelocity().y = 0;
                }
                break;
            case FROMLEFT:
                //Colliding from the left involves the x axis, we need to move in the -x direction to resolve the collision
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x += (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().x -= (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().x = 0;
                    result.ObjectB.GetVelocity().x = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x -= result.PenetrationDepth;
                    result.ObjectA.GetVelocity().x = 0;
                }
                break;
            case FROMRIGHT:
                //colliding from the right means we need to move in the +x direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().x += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().x = 0;
                    result.ObjectB.GetVelocity().x = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x += result.PenetrationDepth;
                    result.ObjectA.GetVelocity().x = 0;
                }
                break;
        }
    }
}
