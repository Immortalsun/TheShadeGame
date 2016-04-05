package GameObject;

import GameObject.Animation.Animation;
import GameObject.Animation.AnimationState;
import GameObject.Projectiles.Fireball;
import GameObject.Projectiles.Projectile;
import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    private Projectile _currentProjectile;

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        Animation[] animations = new Animation[3];
        animations[0] = new Animation(parent,"player1.png", "player1Reversed.png", AnimationState.RUNNING,3, 10);
        animations[1] = new Animation(parent,"playerJump.png", "playerJumpReversed.png", AnimationState.JUMPING,2,10);
        animations[2] = new Animation(parent, "fireballAttack.png", "fireballAttackReversed.png", AnimationState.ATTACKING,5,10);
        BuildAnimator(animations);
    }

    public boolean GetHasCastProjectile()
    {
        return this.GetIsAttacking();
    }

    public void SetHasCastProjectile(boolean hasCast)
    {
        SetIsAttacking(hasCast);
    }

    public Projectile GetCurrentProjectile()
    {
        return _currentProjectile;
    }

    public void Move(PVector moveVector)
    {
        SetVelocity(moveVector);
    }

    public void Jump()
    {
        GetVelocity().y -= 12;
    }

    public void CastProjectile()
    {
        SetIsAttacking(true);
        float startX = 0;
        float startY = 0;

        if(this.GetOrientation() > 0)
        {
            startX = this.GetLocation().x+this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }
        else
        {
            startX = this.GetLocation().x-this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }

        _currentProjectile = new Fireball(startX,startY,this.GetOrientation(),this.GetParent());
    }
}
