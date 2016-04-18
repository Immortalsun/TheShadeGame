package com.shadegame.gameobject;

import com.shadegame.engine.collision.CollisionType;
import com.shadegame.gameobject.animation.*;
import com.shadegame.gameobject.projectiles.Fireball;
import com.shadegame.gameobject.projectiles.Projectile;
import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    private Projectile _currentProjectile;
    private int _health;

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        _health = 1000;
        SetCollisionType(CollisionType.PLAYER);
        Animation[] animations = new Animation[4];
        animations[0] = new RunningAnimation(parent,"player1.png", "player1Reversed.png",3, 10);
        animations[1] = new JumpingAnimation(parent,"playerJump.png", "playerJumpReversed.png",2,10);
        animations[2] = new AttackingAnimation(parent, "fireballAttack.png", "fireballAttackReversed.png",5,10);
        animations[3] = new DamagedAnimation(parent, "playerDamaged.png", "playerDamagedReversed.png",4,10);
        BuildAnimator(animations);
    }

    public boolean GetHasCastProjectile()
    {
        return this.GetIsAttacking() && _currentProjectile != null;
    }

    public void ClearCurrentProjectile()
    {
        _currentProjectile = null;
    }

    public int GetHealth(){ return _health; }

    public void TakeDamage(int damage)
    {
        if(_health - damage > 0)
        {
            _health -= damage;
            SetIsDamaged(true);
        }
        else
        {
            _health = 0;
        }
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

        this.GetVelocity().x = 0;

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
        _currentProjectile.SetCollisionType(CollisionType.PLAYERPROJECTILE);
    }
}
