package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/5/2016.
 */
public class DeadAnimation extends Animation
{

    public DeadAnimation(PApplet parent, String name, String revName, int fCount, int fps) {
        super(parent, name, revName, AnimationState.DEAD, fCount, fps);
    }

    @Override
    public PImage GetNextFrame(boolean isReversed, int moveDirection) {

        PImage frame = null;

        IncrementFrameCounters();

        if(!isCompleted)
        {
            if (!isReversed)
            {
                frame = image.get(0, (currentFrame * 32), 32, 32);
                return frame;
            }
            else
            {
                frame = reversedImage.get(0, (currentFrame * 32), 32, 32);
            }
        }
        else
        {
            if(!isReversed)
            {
                frame = image.get(0, (maxFrames-1)*32, 32, 32);
            }
            else
            {
                frame = reversedImage.get(0, (maxFrames-1)*32, 32, 32);
            }
        }

        return frame;
    }
}
