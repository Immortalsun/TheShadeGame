package GameObject.Projectiles;

import GameObject.Animation.Animation;
import GameObject.Animation.AnimationState;
import GameObject.Animation.DeadAnimation;
import GameObject.Animation.RunningAnimation;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/4/2016.
 */
public class Fireball extends Projectile {
    public Fireball(float x, float y, int direction, PApplet parent)
    {
        super(x, y, 32, 32, 10, 5, 400, direction, parent);


        Animation[] animations = new Animation[2];
        animations[0] = new RunningAnimation(parent,"fireball.png", "fireballReversed.png",3, 5);
        animations[1] = new DeadAnimation(parent, "fireballRangeExplosion.png", "fireballRangeExplosionReversed.png",5,5);

        BuildAnimator(animations);
    }
}
