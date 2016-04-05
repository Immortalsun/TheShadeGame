package GameObject.Animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/5/2016.
 */
public class AttackingAnimation extends Animation {


    public AttackingAnimation(PApplet parent, String name, String revName, int fCount, int fps) {
        super(parent, name, revName, AnimationState.ATTACKING, fCount, fps);
    }

    @Override
    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        PImage frame = null;

        IncrementFrameCounters();

        if (!isReversed)
        {
            frame = image.get(0, (currentFrame * 32), 32, 32);
            return frame;
        }
        else
        {
            frame = reversedImage.get(0, (currentFrame * 32), 32, 32);
        }

        return frame;
    }
}
