package com.shadegame.gameobject.animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/3/2016.
 */


public class Animation {

    public String imageName;
    public String reversedImageName;
    public PImage image, reversedImage;
    private Animation childAnim;
    public AnimationState associatedState;
    public int maxFrames, desiredFps, currentFrame, frameCounter, completionCounter;
    public boolean isCompleted, hasChildAnimation, isLooping;

    public Animation(PApplet parent, String name, String revName, AnimationState state, int fCount, int fps)
    {

        imageName = name;
        reversedImageName = revName;
        associatedState = state;
        image = parent.loadImage(imageName);
        if(reversedImageName.isEmpty())
        {
            reversedImageName = imageName;
        }
        reversedImage = parent.loadImage(reversedImageName);
        maxFrames = fCount;
        desiredFps = fps;
        currentFrame = 0;
        frameCounter = 1;
        completionCounter = 0;

        if(state.equals(AnimationState.CHARGED) ||
                state.equals(AnimationState.RUNNING) ||
                state.equals(AnimationState.IDLE))
        {
            isLooping = true;
        }
    }

    public Animation(PImage image, PImage revImage, AnimationState state, int fCount, int fps)
    {
        associatedState = state;
        this.image = image;

        if(revImage == null)
        {
            reversedImage = image;
        }
        else
        {
            reversedImage = revImage;
        }

        maxFrames = fCount;
        desiredFps = fps;
        currentFrame = 0;
        frameCounter = 1;
        completionCounter = 0;

        if(state.equals(AnimationState.CHARGED) ||
                state.equals(AnimationState.RUNNING) ||
                state.equals(AnimationState.IDLE))
        {
            isLooping = true;
        }
    }

    public void SetChildAnim(Animation anim)
    {
        childAnim = anim;
        hasChildAnimation = true;
    }

    public void SetIsLooping(boolean value)
    {
        isLooping = value;
    }

    public Animation GetChildAnimation()
    {
        return childAnim;
    }

    public boolean GetIsCompleted()
    {
        return isCompleted;
    }

    public PImage GetNextFrame(boolean isReversed, int moveDirection)
    {
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
            
            if(!isLooping)
            {
                currentFrame = 0;
                frameCounter = 1;
            }

        }
    }



}
