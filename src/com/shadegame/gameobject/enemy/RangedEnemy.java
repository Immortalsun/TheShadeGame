package com.shadegame.gameobject.enemy;

import com.shadegame.gameobject.player.AttackType;
import com.shadegame.gameobject.projectiles.Projectile;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/14/2016.
 */
public class RangedEnemy extends Enemy {

    protected Projectile _currentProjectile;

    public RangedEnemy(float x, float y, float objectWidth, float objectHeight, PApplet parent, String spawnId, int health) {
        super(x, y, objectWidth, objectHeight, parent, spawnId, health);
    }


    public Projectile GetCurrentProjectile()
    {
        return _currentProjectile;
    }

    public void ClearCurrentProjectile()
    {
        SetIsAttacking(false, AttackType.NONE);
        _currentProjectile = null;
    }

    @Override
    public void Attack() {

    }

    @Override
    public void Jump() {

    }
}
