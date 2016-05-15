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
}
