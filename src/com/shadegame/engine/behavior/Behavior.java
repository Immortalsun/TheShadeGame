package com.shadegame.engine.behavior;

import com.shadegame.gameobject.GameObject;

/**
 * Created by Maashes on 4/10/2016.
 */
public abstract class Behavior
{
    protected GameObject object;

    public Behavior(GameObject obj)
    {
        object = obj;
    }

    public abstract void DoNextAction();
}
