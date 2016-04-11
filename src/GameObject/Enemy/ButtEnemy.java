package GameObject.Enemy;

import GameObject.Animation.Animation;
import GameObject.Animation.AttackingAnimation;
import GameObject.Animation.RunningAnimation;
import GameObject.Projectiles.Fireball;
import GameObject.Projectiles.NoxiousBall;
import GameObject.Projectiles.Projectile;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public class ButtEnemy extends Enemy
{

    private Projectile _currentProjectile;
    private boolean _attacked;

    public ButtEnemy(float x, float y, float objectWidth, float objectHeight, PApplet parent, String spawnId, int health) {
        super(x, y, objectWidth, objectHeight, parent, spawnId, health);

        SetType(EnemyType.RANGED);
        Animation[] animations = new Animation[2];
        animations[0] = new RunningAnimation(parent, "scootieBootie.png","scootieBootieReversed.png",3, 10);
        animations[1] = new AttackingAnimation(parent,"scootieAttack.png", "scootieAttackReversed.png",5,10);

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
    }
}
