package GameObject;

import processing.core.PImage;
import processing.core.PApplet;
/**
 * Created by Maashes on 4/1/2016.
 */
public class Animator {

    private GameObject object;
    private PImage image, flippedImage, frame;
    private PApplet sketchParent;
    private int frameCount, desiredFps, currentFrame, maxFrames;
    private float lastDirection;

    public Animator(GameObject obj, PApplet parent ,String imagePath, String flippedImagePath, int fCount, int fps)
    {
        sketchParent = parent;
        object = obj;
        image = sketchParent.loadImage(imagePath);
        flippedImage = sketchParent.loadImage(flippedImagePath);
        frameCount = 1;
        maxFrames = fCount;
        desiredFps = fps;
    }

    public void Animate()
    {
        if(image != null) {
            if (object.GetVelocity().x != 0) {
                if (frameCount % desiredFps == 0) {
                    if (currentFrame < maxFrames - 1) {
                        currentFrame++;
                    } else {
                        currentFrame = 0;
                    }

                    frameCount = 1;
                } else {
                    frameCount++;
                }
                lastDirection = object.GetVelocity().x;
            } else {
                currentFrame = 0;
                frameCount = 1;
            }

            if (object.GetVelocity().x > 0) {
                frame = image.get(0, (currentFrame * 32), 32, 32);
            } else if (object.GetVelocity().x < 0) {
                frame = flippedImage.get(0, (currentFrame * 32), 32, 32);
            } else {
                if (lastDirection >= 0) {
                    frame = image.get(0, 0, 32, 32);
                } else {
                    frame = flippedImage.get(0, 0, 32, 32);
                }

            }

            sketchParent.image(frame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());
        }
    }

}
