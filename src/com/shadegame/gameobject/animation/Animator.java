package com.shadegame.gameobject.animation;

import com.shadegame.engine.EngineProvider;
import com.shadegame.gameobject.GameObject;
import com.shadegame.gameobject.player.Player;
import processing.core.PImage;
import processing.core.PApplet;

import java.util.HashMap;

/**
 * Created by Maashes on 4/1/2016.
 */
public class Animator {

    private GameObject object;
    private PImage frame, lastFrame, lastTriggerFrame, triggerFrame;
    private PApplet sketchParent;
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
            _animations.put(anim.AssociatedState, anim);
        }
    }

    public void DoAnimation(AnimationState state)
    {
        if(EngineProvider.GetDefaultEngineInstance().GetIsPaused())
        {
            sketchParent.image(lastFrame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());
            if(lastTriggerFrame != null)
            {
                sketchParent.image(lastTriggerFrame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());
            }
            return;
        }

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

        if(anim == null)
        {
            anim = _animations.get(AnimationState.RUNNING);
        }

        frame = anim.GetNextFrame(isReversed, moveDirection);

        lastFrame = frame;

        if(object.GetVelocity().x != 0)
        {
            lastDirection = object.GetVelocity().x;
        }

        float xLoc = object.GetLocation().x;
        float yLoc = object.GetLocation().y;

        if(anim.HasSpecifiedLocation())
        {
            xLoc = anim.GetAnimationX(object);
            yLoc = anim.GetAnimationY(object);
        }

        sketchParent.image(frame, xLoc, yLoc, frame.width, frame.height);

        if(object.GetIsAnimTriggered())
        {
            triggerFrame = object.GetTriggeredAnimationFrame();

            if(triggerFrame != null)
            {
                sketchParent.image(triggerFrame, object.GetLocation().x, object.GetLocation().y, object.GetWidth(), object.GetHeight());
            }
            lastTriggerFrame = triggerFrame;
        }


        if(anim.GetIsCompleted())
        {
            object.SetStateBasedOnAnimationCompletion();
        }


    }

}
