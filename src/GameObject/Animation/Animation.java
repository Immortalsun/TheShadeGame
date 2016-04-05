package GameObject.Animation;

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
    public int maxFrames, desiredFps, currentFrame, frameCounter;
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
    }

    public boolean GetIsCompleted()
    {
        return isCompleted;
    }

    public abstract PImage GetNextFrame(boolean isReversed, int moveDirection);

    public void IncrementFrameCounters()
    {
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

            isCompleted = true;
            frameCounter = 1;
        }
        else
        {
            isCompleted = false;
            frameCounter++;
        }
    }



}
