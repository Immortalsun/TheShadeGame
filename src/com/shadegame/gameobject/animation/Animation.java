package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/3/2016.
 */


public abstract class Animation {

    public String imageName;
    public String reversedImageName;
    public PImage image, reversedImage;
    public AnimationState associatedState;
    public int maxFrames, desiredFps, currentFrame, frameCounter, completionCounter;
    public boolean isCompleted;

    public Animation(PApplet parent, String name, String revName, AnimationState state, int fCount, int fps)
    {

        imageName = name;
        reversedImageName = revName;
        associatedState = state;
        image = parent.loadImage(imageName);
        reversedImage = parent.loadImage(reversedImageName);
        maxFrames = fCount;
        desiredFps = fps;
        currentFrame = 0;
        frameCounter = 1;
        completionCounter = 0;
    }

    public boolean GetIsCompleted()
    {
        return isCompleted;
    }

    public abstract PImage GetNextFrame(boolean isReversed, int moveDirection);

    public void IncrementFrameCounters()
    {
        isCompleted = false;
        if (frameCounter % desiredFps == 0)
        {
            if (currentFrame < maxFrames - 1)
            {
                currentFrame++;
            }
            else
            {
                currentFrame = 0;
            }
            frameCounter = 1;
            completionCounter++;
        }
        else
        {
            frameCounter++;
        }

        if(completionCounter == maxFrames-1)
        {
            isCompleted = true;
            completionCounter = 0;
            
            if(!associatedState.equals(AnimationState.RUNNING))
            {
                currentFrame = 0;
                frameCounter = 1;
            }

        }
    }



}