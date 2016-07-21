package com.shadegame.gameobject.projectiles;

import processing.core.PApplet;

/**
 * Created by Maashes on 7/14/2016.
 */
public class MeleeAttack extends Projectile {

    public MeleeAttack(float x, float y, int direction, PApplet parent) {
        super(x, y, 32, 32, 50, 0, 50, direction, parent);
    }

    public void SetStateBasedOnAnimationCompletion()
    {
        SetIsReadyForCleanup(true);
    }

}
