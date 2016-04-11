package GameObject.Projectiles;

import GameObject.Animation.Animation;
import GameObject.Animation.DeadAnimation;
import GameObject.Animation.RunningAnimation;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public class NoxiousBall extends Projectile
{
    public NoxiousBall(float x, float y, int direction, PApplet parent) {
        super(x, y, 32, 32, 25, 5, 300, direction, parent);

        Animation[] animations = new Animation[2];
        animations[0] = new RunningAnimation(parent,"noxiousBall.png", "noxiousBall.png",4, 10);
        animations [1] = new DeadAnimation(parent,"noxiousBall.png", "noxiousBall.png",4, 1);

        BuildAnimator(animations);
    }
}
