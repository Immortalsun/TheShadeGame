package com.shadegame.gameobject.projectiles;

import com.shadegame.gameobject.animation.Animation;
import com.shadegame.gameobject.animation.DeadAnimation;
import com.shadegame.gameobject.animation.RunningAnimation;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public class NoxiousBall extends Projectile
{
    public NoxiousBall(float x, float y, int direction, PApplet parent) {
        super(x, y, 32, 32, 25, 5, 300, direction, parent);

        Animation[] animations = new Animation[2];
        animations[0] = new RunningAnimation(parent,"ProjectileSprites/noxiousBall.png", "ProjectileSprites/noxiousBall.png",4, 10);
        animations [1] = new DeadAnimation(parent,"ProjectileSprites/noxiousBallExplosion.png", "ProjectileSprites/noxiousBallExplosion.png",4, 5);

        BuildAnimator(animations);
    }
}
