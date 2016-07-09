package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/5/2016.
 */
public class ProjectileAttackingAnimation extends Animation {


    public ProjectileAttackingAnimation(PApplet parent, String name, String revName, int fCount, int fps) {
        super(parent, name, revName, AnimationState.ATTACKING, fCount, fps);
    }

    @Override
    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        PImage frame = null;

        IncrementFrameCounters();

        if (!isReversed)
        {
            frame = Image.get(0, (GetCurrentFrame() * 32), 32, 32);

            return frame;
        }
        else
        {
            frame = reversedImage.get(0, (GetCurrentFrame() * 32), 32, 32);
        }

        return frame;
    }
}
