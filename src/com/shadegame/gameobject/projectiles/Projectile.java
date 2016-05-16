package com.shadegame.gameobject.projectiles;

import com.shadegame.gameobject.GameObject;
import com.shadegame.gameobject.animation.Animation;
import com.shadegame.gameobject.animation.AnimationState;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/4/2016.
 */
public class Projectile extends GameObject {

    private int damage;
    private float flightSpeed;
    private float range;
    private float originX, originY;

    public Projectile(float x, float y, float objectWidth, float objectHeight, int damage, float speed, float range, int direction,PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        this.damage = damage;
        this.flightSpeed = speed;
        this.range = range;
        originX = x;
        originY = y;
        if(direction > 0)
        {
            this.GetVelocity().x += flightSpeed;
        }
        else
        {
            this.GetVelocity().x -= flightSpeed;
        }
    }

    public void BuildProjectileAnimator(String runningAnimImage, String runningAnimReversed, int runningFrames, int runningSpeed, String destroyedAnimImage, String destroyedAnimReversed,int destroyedFrames, int destroyedSpeed)
    {
        Animation[] anims = new Animation[2];
        anims[0] = new Animation(GetParent(),runningAnimImage,runningAnimReversed, AnimationState.RUNNING,runningFrames,runningSpeed);
        anims[1] = new Animation(GetParent(),destroyedAnimImage,destroyedAnimReversed,AnimationState.DEAD, destroyedFrames,destroyedSpeed);
        BuildAnimator(anims);
    }

    public int GetDamage()
    {
        return damage;
    }

    public float GetFlightSpeed()
    {
        return flightSpeed;
    }

    public float GetRange()
    {
        return range;
    }

    public void CheckRange()
    {
        if(Math.abs(originX - this.GetLocation().x) > range)
        {
            this.SetIsDestroyed(true);
        }
    }
}
