package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/5/2016.
 */
public class JumpingAnimation extends Animation {


    public JumpingAnimation(PApplet parent, String name, String revName, int fCount, int fps) {
        super(parent, name, revName, AnimationState.JUMPING, fCount, fps);
    }

    @Override
    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        PImage frame = null;

        IncrementFrameCounters();

        if(moveDirection < 0)
        {
            if(isReversed)
            {
                frame = reversedImage.get(0,0,32,32);
                return frame;
            }
            else
            {
                frame = image.get(0,0,32,32);
                return frame;
            }
        }
        else
        {
            if(isReversed)
            {
                frame = reversedImage.get(0,32,32,32);
                return frame;
            }
            else
            {
                frame = image.get(0,32,32,32);
                return frame;
            }
        }
    }
}
