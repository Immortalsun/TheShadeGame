package com.shadegame.gameobject.enemy;

import com.shadegame.engine.collision.CollisionType;
import com.shadegame.engine.EngineProvider;
import com.shadegame.gameobject.animation.Animation;
import com.shadegame.gameobject.animation.AttackingAnimation;
import com.shadegame.gameobject.animation.RunningAnimation;
import com.shadegame.gameobject.projectiles.NoxiousBall;
import com.shadegame.gameobject.projectiles.Projectile;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public class ButtEnemy extends RangedEnemy

{

    private int _attackCounter;

    public ButtEnemy(float x, float y, float objectWidth, float objectHeight, PApplet parent, String spawnId, int health) {
        super(x, y, objectWidth, objectHeight, parent, spawnId, health);

        SetType(EnemyType.RANGED);
        Animation[] animations = new Animation[2];
        animations[0] = new RunningAnimation(parent, "scootieBootie.png","scootieBootieReversed.png",3, 10);
        animations[1] = new AttackingAnimation(parent,"scootieAttack.png", "scootieAttackReversed.png",5,10);
        _attackCounter = 0;
        BuildAnimator(animations);

    }

    @Override
    public void Attack()
    {
        _attackCounter++;
        if(_attackCounter == 60)
        {
            if(EngineProvider.GetDefaultEngineInstance().GetPlayerLocation().x >= this.GetLocation().x)
            {
                this.SetOrientation(1);
            }
            else
            {
                this.SetOrientation(-1);
            }

            SetIsAttacking(true);
            float startX = 0;
            float startY = 0;

            if (this.GetOrientation() > 0) {
                startX = this.GetLocation().x + this.GetWidth();
                startY = (this.GetLocation().y + this.GetHeight() / 2) - 20;
            } else {
                startX = this.GetLocation().x - this.GetWidth();
                startY = (this.GetLocation().y + this.GetHeight() / 2) - 20;
            }

            _currentProjectile = new NoxiousBall(startX, startY, this.GetOrientation(), this.GetParent());
            _currentProjectile.SetCollisionType(CollisionType.ENEMYPROJECTILE);
            _attackCounter = 0;
        }

    }

    @Override
    public void Jump()
    {
        if(!GetIsJumping())
        {
            this.SetIsJumping(true);
            this.GetVelocity().y -= 10;
        }

    }
}
