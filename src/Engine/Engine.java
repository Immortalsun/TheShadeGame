package Engine;

/**
 * Created by Maashes on 3/30/2016.
 */
import GameObject.*;
import processing.core.PApplet;
import java.util.ArrayList;

public class Engine
{
    private int screenWidth;
    private int screenHeight;
    private ArrayList<GameObject> _gameObjectCollection;
    private GameObject ground;
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
        Player player = new Player(x,y, objectWidth, objectHeight, this.sketchParent);
        player.SetIsPlayer(true);
        _gameObjectCollection.add(player);
        return player;
    }

    public void SetGroundLevel()
    {
        ground = new GameObject(0,screenHeight-15, screenWidth-1, 15, this.sketchParent);
        ground.SetIsOnGround(true);
        _gameObjectCollection.add(ground);
    }

    public void GeneratePlatforms()
    {
        Platform p = new Platform(200, ground.GetMinY() - 105, 50, 15, this.sketchParent);
        p.SetIsOnGround(true);
        _gameObjectCollection.add(p);
    }

    public void Update()
    {
        for(GameObject g : _gameObjectCollection)
        {
            if(g.GetIsPlayer()) //update all non player locations normally
            {
                if(!g.GetIsOnGround())
                {
                    if(g.GetVelocity().y < 10)
                    {
                        g.GetVelocity().y+=gravityConstant;
                    }
                }
                else
                {
                    g.SetIsJumping(false);
                    g.GetVelocity().y = 0;
                    //we need better collision resolution
                    g.GetLocation().y = (ground.GetMinY() - g.GetHeight());
                }
                g.Update();
                g.SetIsOnGround(CheckOnGround(g));
                return;
            }
            g.Update();
        }
    }

    public void Display()
    {
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

    public boolean CheckOnGround(GameObject obj)
    {
        if(obj.GetMaxY() < ground.GetMinY()) return false;

        return true;
    }

    public boolean CheckOnPlatform(GameObject obj, Platform platform)
    {
        if(obj.GetMaxY() == platform.GetMinY() &&
                (obj.GetMinX() >= platform.GetMinX() || obj.GetMaxX() <= platform.GetMaxX()))
        {
            return true;
        }

        return false;
    }

    public void ResolveCollision(GameObject objA, GameObject objB)
    {

    }
}
