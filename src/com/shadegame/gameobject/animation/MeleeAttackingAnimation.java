package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 7/9/2016.
 */
public class MeleeAttackingAnimation extends Animation
{
    public MeleeAttackingAnimation(PApplet parent, String name, String revName, int fCount, int fps) {
        super(parent, name, revName, AnimationState.MELEEATTACK, fCount, fps);
    }

    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        PImage frame = null;

        IncrementFrameCounters();

        if (!isReversed)
        {
            frame = Image.get(0, (GetCurrentFrame() * 32), 48, 32);

            return frame;
        }
        else
        {
            frame = reversedImage.get(0, (GetCurrentFrame() * 32), 48, 32);
        }

        return frame;
    }
}
