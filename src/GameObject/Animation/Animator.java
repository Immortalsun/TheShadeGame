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

    public void DoAnimation(AnimationState state)
    {
        Animation anim = _animations.get(state);

        boolean isReversed = (object.GetVelocity().x < 0) || lastDirection < 0;
        boolean isMoving = object.GetVelocity().x != 0 || object.GetVelocity().y != 0.5;
        int moveDirection = 0;

        if(isReversed)
        {
            object.SetOrientation(-1);
        }
        else
        {
            object.SetOrientation(1);
        }

        if(isMoving)
        {
            moveDirection = 1;

            if(object.GetVelocity().y > 0)
            {
                moveDirection = 2;
            }
            else if(object.GetVelocity().y < 0)
            {
                moveDirection = -2;
            }
        }


        frame = anim.GetNextFrame(isReversed, moveDirection);

        if(object.GetVelocity().x != 0)
        {
            lastDirection = object.GetVelocity().x;
        }

        sketchParent.image(frame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());

        if(anim.GetIsCompleted())
        {
            object.SetStateBasedOnAnimationCompletion();
        }

    }

}
