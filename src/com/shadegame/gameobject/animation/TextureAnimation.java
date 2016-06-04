package com.shadegame.gameobject.animation;

import processing.core.PImage;

/**
 * Created by Maashes on 6/4/2016.
 */
public class TextureAnimation extends Animation
{
    public TextureAnimation(PImage image)
    {
        super(image, null, AnimationState.IDLE, 1, 100);
    }

    @Override
    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        return image;
    }
}
