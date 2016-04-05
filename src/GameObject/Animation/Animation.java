package GameObject.Animation;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Maashes on 4/3/2016.
 */


public class Animation {

    public String imageName;
    public String reversedImageName;
    public PImage image, reversedImage;
    public AnimationState associatedState;
    public int maxFrames, desiredFps;

    public Animation(PApplet parent, String name, String revName, AnimationState state, int fCount, int fps)
    {

        imageName = name;
        reversedImageName = revName;
        associatedState = state;
        image = parent.loadImage(imageName);
        reversedImage = parent.loadImage(reversedImageName);
        maxFrames = fCount;
        desiredFps = fps;
    }

}
