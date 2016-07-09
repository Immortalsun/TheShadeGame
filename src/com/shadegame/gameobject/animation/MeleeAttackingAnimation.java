package com.shadegame.gameobject.animation;

import com.shadegame.gameobject.GameObject;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 7/9/2016.
 */
public class MeleeAttackingAnimation extends Animation
{
    private boolean _isReversed;

    public MeleeAttackingAnimation(PApplet parent, String name, String revName, int fCount, int fps)
    {
        super(parent, name, revName, AnimationState.MELEEATTACK, fCount, fps);

    }

    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
        PImage frame = null;

        IncrementFrameCounters();

        if (!isReversed)
        {
            frame = Image.get(0, (GetCurrentFrame() * 32), 48, 32);
            _isReversed = false;
            return frame;
        }
        else
        {
            frame = reversedImage.get(0, (GetCurrentFrame() * 32), 48, 32);
            _isReversed = true;
        }

        return frame;
    }

    public float GetAnimationX(GameObject associatedObj)
    {
        if(!_isReversed)
        {
            return associatedObj.GetLocation().x;
        }
        else{
            return associatedObj.GetLocation().x-associatedObj.GetWidth()/2;
        }
    }

    public float GetAnimationY(GameObject associatedObj)
    {
        if(!_isReversed)
        {
            return associatedObj.GetLocation().y;
        }
        else
        {
            return associatedObj.GetLocation().y;
        }
    }
}
