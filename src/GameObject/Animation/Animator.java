package GameObject.Animation;

import GameObject.GameObject;
import processing.core.PImage;
import processing.core.PApplet;

import java.util.HashMap;

/**
 * Created by Maashes on 4/1/2016.
 */
public class Animator {

    private GameObject object;
    private PImage frame;
    private PApplet sketchParent;
    private int frameCount, currentFrame, maxFrames;
    private float lastDirection;
    private HashMap<AnimationState, Animation> _animations;

    public Animator(GameObject obj, PApplet parent ,Animation[] animations)
    {
        sketchParent = parent;
        object = obj;
        _animations = new HashMap<AnimationState, Animation>();
        BuildAnimationMap(animations);
    }

    private void BuildAnimationMap(Animation[] animations)
    {
        for(Animation anim : animations)
        {
            _animations.put(anim.associatedState, anim);
        }
    }

    public void Animate()
    {
        AnimationState state = object.GetCurrentAnimationState();

        Animation anim = _animations.get(state);

        if(anim.image != null) {
            if (object.GetVelocity().x != 0) {
                if (frameCount % anim.desiredFps == 0) {
                    if (currentFrame < anim.maxFrames - 1) {
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

            boolean isReversed = (object.GetVelocity().x < 0) || lastDirection < 0;
            boolean isMoving = object.GetVelocity().x != 0;

            if(state == AnimationState.RUNNING)
            {
                if (isMoving)
                {
                    if(!isReversed)
                    {
                        frame = anim.image.get(0, (currentFrame * 32), 32, 32);
                    }
                    else
                    {
                        frame = anim.reversedImage.get(0, (currentFrame * 32), 32, 32);
                    }

                }
                else
                {
                    if (!isReversed)
                    {
                        frame = anim.image.get(0, 0, 32, 32);
                    }
                    else
                    {
                        frame = anim.reversedImage.get(0, 0, 32, 32);
                    }
                }
            }
            else if(state == AnimationState.JUMPING)
            {
                if(!isReversed)
                {
                    if(object.GetVelocity().y < 0)
                    {
                        frame = anim.image.get(0,0,32,32);
                    }
                    else
                    {
                        frame = anim.image.get(0,32,32,32);
                    }
                }
                else
                {
                    if(object.GetVelocity().y < 0)
                    {
                        frame = anim.reversedImage.get(0,0,32,32);
                    }
                    else
                    {
                        frame = anim.reversedImage.get(0,32,32,32);
                    }
                }
            }

            if(isReversed)
            {
                object.SetOrientation(-1);
            }
            else
            {
                object.SetOrientation(1);
            }

            sketchParent.image(frame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());
        }
    }

}
