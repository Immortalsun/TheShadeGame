package com.shadegame.gameobject;

/**
 * Created by Maashes on 3/30/2016.
 */
import com.shadegame.engine.EngineProvider;
import com.shadegame.engine.collision.CollisionType;
import com.shadegame.gameobject.animation.Animation;
import com.shadegame.gameobject.animation.AnimationState;
import com.shadegame.gameobject.animation.Animator;
import com.shadegame.gameobject.player.AttackType;
import processing.core.*;

import static processing.core.PConstants.RECT;

public class GameObject
{
    //Fields
    private PShape boundingRect;
    private PVector location;
    private PVector velocity;
    private PApplet sketchParent;
    private CollisionType collisionType;
    private AttackType currentAttackType;
    private Animator animator;
    private int strokeColor, orientation;
    private float minY, minX, objWidth, objHeight;
    private boolean isPlayer, isOnGround, isGround,
            isJumping, isDestroyed, isAttacking,
            readyForCleanup, isDamaged, animationTriggered;
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
        strokeColor = sketchParent.color(0,0,0);
        orientation = 1;
        boundingRect.setFill(false);
        boundingRect.setStroke(strokeColor);
    }

    //Properties
    public PShape GetBoundingRect()
    {
        return boundingRect;
    }

    public void SetCollisionType(CollisionType type)
    {
        collisionType = type;
    }

    public CollisionType GetCollisionType()
    {
        return collisionType;
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

    public boolean GetIsReadyForCleanup() {return readyForCleanup; }

    public boolean GetIsDamaged() {return isDamaged;}

    public boolean GetIsAnimTriggered()
    {
        return animationTriggered;
    }

    public void SetIsAnimTriggered(boolean value)
    {
        animationTriggered = value;
    }

    public void SetIsAttacking(boolean attacking, AttackType type)
    {
        isAttacking = attacking;
        if(attacking)
        {
            currentAttackType = type;
        }
        else{
            currentAttackType = AttackType.NONE;
        }
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

    public void SetIsDamaged(boolean damaged) {isDamaged = damaged;}

    public PImage GetTriggeredAnimationFrame()
    {
        return null;
    }

    public void SetIsDestroyed(boolean destroyed)
    {
        isDestroyed = destroyed;

        if(isDestroyed)
        {
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    public void SetIsReadyForCleanup(boolean cleanup)
    {
        readyForCleanup = cleanup;
    }

    public void BuildAnimator(Animation[] animations)
    {
        animator = new Animator(this, sketchParent, animations);
    }

    public AnimationState GetCurrentAnimationState()
    {
        if(isAttacking)
        {
            if(currentAttackType.equals(AttackType.BLAST))
            {
                return AnimationState.ATTACKING;
            }
            else if(currentAttackType.equals(AttackType.MELEE)){
                return AnimationState.MELEEATTACK;
            }
            else if(currentAttackType.equals(AttackType.EXPLOSION))
            {
                return AnimationState.AOEATTACK;
            }

        }

        if(isDamaged)
        {
            return AnimationState.DAMAGED;
        }

        if(isDestroyed)
        {
            return AnimationState.DEAD;
        }

        if(isJumping)
        {
            return AnimationState.JUMPING;
        }

        if(isGround)
        {
            return AnimationState.IDLE;
        }

        if (isOnGround) {
            return AnimationState.RUNNING;
        }
        return AnimationState.RUNNING;
    }

    public void SetStateBasedOnAnimationCompletion()
    {
        if(isAttacking || GetCurrentAnimationState().equals(AnimationState.ATTACKING))
        {
            isAttacking = false;
        }

        if(isDestroyed || GetCurrentAnimationState().equals(AnimationState.DEAD))
        {
            readyForCleanup = true;
        }

        if(isDamaged || GetCurrentAnimationState().equals(AnimationState.DAMAGED))
        {
            isDamaged = false;
        }
    }
    //Methods
    //Draw boundingRect
    public void Display()
    {
        if(animator != null)
        {
            animator.DoAnimation(GetCurrentAnimationState());
        }

        if(EngineProvider.GetDefaultEngineInstance().GetIsEngineDebugMode())
        {
            sketchParent.shape(boundingRect,location.x,location.y);
        }
    }

    //Update any Movement
    public void Update()
    {
        location.add(velocity);
    }
}
