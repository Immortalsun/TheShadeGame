package GameObject;

/**
 * Created by Maashes on 3/30/2016.
 */
import GameObject.Animation.Animation;
import GameObject.Animation.AnimationState;
import GameObject.Animation.Animator;
import processing.core.*;

import static processing.core.PConstants.RECT;

public class GameObject
{
    //Fields
    private PShape boundingRect;
    private PVector location;
    private PVector velocity;
    private PApplet sketchParent;
    private Animator animator;
    private int fillColor, orientation;
    private float minY, minX, objWidth, objHeight;
    private boolean isPlayer, isOnGround, isGround, isJumping, isDestroyed, isAttacking;
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
        orientation = 1;
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

    public boolean GetIsDestroyed()
    {
        return isDestroyed;
    }

    public boolean GetIsAttacking()
    {
        return isAttacking;
    }

    public void SetIsAttacking(boolean attacking)
    {
        isAttacking = attacking;
    }

    public PApplet GetParent()
    {
        return sketchParent;
    }

    public int GetOrientation()
    {
        return orientation;
    }

    public void SetOrientation(int o)
    {
        orientation = o;
    }

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

    public void SetIsDestroyed(boolean destroyed)
    {
        isDestroyed = destroyed;
    }

    public void BuildAnimator(Animation[] animations)
    {
        animator = new Animator(this, sketchParent, animations);
    }

    public AnimationState GetCurrentAnimationState()
    {
        if (isOnGround) {
           return AnimationState.RUNNING;
        }

        if(isJumping)
        {
            return AnimationState.JUMPING;
        }

        if(isDestroyed)
        {
            return AnimationState.DEAD;
        }

        if(isAttacking)
        {
            return AnimationState.ATTACKING;
        }

        return AnimationState.RUNNING;
    }
    //Methods
    //Draw boundingRect
    public void Display()
    {
        if(animator != null)
        {
            animator.Animate();
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
