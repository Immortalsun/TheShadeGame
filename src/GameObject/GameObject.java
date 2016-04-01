package GameObject;

/**
 * Created by Maashes on 3/30/2016.
 */
import processing.core.*;

import static processing.core.PConstants.CORNER;
import static processing.core.PConstants.RECT;

public class GameObject
{
    //Fields
    private PShape boundingRect;
    private PVector location;
    private PVector velocity;
    private PApplet sketchParent;
    private PImage image;
    private PImage frame;
    private int fillColor;
    private int frameCount, currentFrame, desiredFramesPerSecond, maxAnimFrames;
    private float minY, minX, objWidth, objHeight;
    private boolean isPlayer;
    private boolean isOnGround;
    private boolean isGround;
    private boolean isJumping;
    //Constructor
    public GameObject(float x, float y, float objectWidth, float objectHeight, PApplet parent)
    {
        location = new PVector(x, y);
        sketchParent = parent;
        boundingRect = sketchParent.createShape(RECT, 0, 0, objectWidth, objectHeight);
        minX = location.x;
        minY = location.y;
        objWidth = objectWidth;
        objHeight = objectHeight;
        isPlayer = false;
        velocity = new PVector(0,0);
        fillColor = sketchParent.color(168,230,201);
        boundingRect.setFill(fillColor);
    }

    //Properties
    public PShape GetBoundingRect()
    {
        return boundingRect;
    }

    public float GetHeight()
    {
        return objHeight;
    }

    public float GetWidth()
    {
        return objWidth;
    }

    public float GetMinY()
    {
        return location.y;
    }

    public float GetMinX()
    {
        return location.x;
    }

    public float GetMaxY()
    {
        return location.y+objHeight;
    }

    public float GetMaxX()
    {
        return location.x+objWidth;
    }

    public PVector GetLocation()
    {
        return location;
    }

    public PVector GetVelocity()
    {
        return velocity;
    }

    public boolean GetIsPlayer()
    {
        return isPlayer;
    }

    public boolean GetIsOnGround()
    {
        return isOnGround;
    }

    public boolean GetIsJumping()
    {
        return isJumping;
    }

    public boolean GetIsGround() {return isGround; }

    public void SetVelocity(PVector v)
    {
        velocity = v;
    }

    public void SetIsPlayer(boolean isPlyr)
    {
        isPlayer = isPlyr;
    }

    public void SetIsOnGround(boolean onGround)
    {
        isOnGround = onGround;
    }

    public void SetIsJumping(boolean jumping)
    {
        isJumping = jumping;
    }

    public void SetIsGround(boolean ground) { isGround = ground; }

    public void SetImage(String imagePath, int fCount, int desiredFps)
    {
        image = sketchParent.loadImage(imagePath);
        maxAnimFrames = fCount;
        currentFrame = 0;
        frameCount = 1;
        desiredFramesPerSecond = desiredFps;
    }


    //Methods
    //Draw boundingRect
    public void Display()
    {
        if(image != null)
        {
            if(frameCount % desiredFramesPerSecond == 0)
            {
                if(currentFrame < maxAnimFrames-1)
                {
                    currentFrame++;
                }
                else
                {
                    currentFrame = 0;
                }

                frameCount = 1;
            }
            else
            {
                frameCount++;
            }

            frame = image.get(0,(currentFrame*32), 32, 32);

            sketchParent.image(frame, location.x, location.y, objWidth, objHeight, 0,0,32,32);
        }
        else
        {
            sketchParent.shape(boundingRect, location.x, location.y);
        }
    }

    //Update any Movement
    public void Update()
    {
        location.add(velocity);
    }
}
