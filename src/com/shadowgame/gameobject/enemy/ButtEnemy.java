package com.shadowgame.gameobject.enemy;

import com.shadowgame.engine.Collision.CollisionType;
import com.shadowgame.engine.EngineProvider;
import com.shadowgame.gameobject.animation.Animation;
import com.shadowgame.gameobject.animation.AttackingAnimation;
import com.shadowgame.gameobject.animation.RunningAnimation;
import com.shadowgame.gameobject.projectiles.NoxiousBall;
import com.shadowgame.gameobject.projectiles.Projectile;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public class ButtEnemy extends Enemy
{

    private Projectile _currentProjectile;
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

    public Projectile GetCurrentProjectile()
    {
        return _currentProjectile;
    }

    public void ClearCurrentProjectile()
    {
        SetIsAttacking(false);
        _currentProjectile = null;
    }

    @Override
    public void Attack()
    {
        _attackCounter++;
        if(_attackCounter == 60)
        {
            if(this.GetOrientation() == EngineProvider.GetDefaultEngineInstance().GetPlayerOrientation())
            {
                this.SetOrientation(-(EngineProvider.GetDefaultEngineInstance().GetPlayerOrientation()));
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
