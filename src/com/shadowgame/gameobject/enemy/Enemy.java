package com.shadowgame.gameobject.enemy;

import com.shadowgame.engine.behavior.Behavior;
import com.shadowgame.engine.collision.CollisionType;
import com.shadowgame.gameobject.GameObject;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/10/2016.
 */
public abstract class Enemy extends GameObject
{

    private EnemyType _type;
    private Behavior _behavior;
    private int _health;
    private String _spawnID;

    public Enemy(float x, float y, float objectWidth, float objectHeight, PApplet parent, String spawnId, int health)
    {
        super(x, y, objectWidth, objectHeight, parent);
        _spawnID = spawnId;
        _health = health;
        SetCollisionType(CollisionType.ENEMY);
    }

    public EnemyType GetType()
    {
        return _type;
    }

    public void SetType(EnemyType type)
    {
        _type = type;
    }

    public String GetSpawnID()
    {
        return _spawnID;
    }

    public Behavior GetBehavior()
    {
        return _behavior;
    }

    public void SetBehavior(Behavior b)
    {
        _behavior = b;
    }

    public int GetCurrentHealth()
    {
        return _health;
    }

    public void TakeDamage(int damage)
    {
        _health -= damage;

        if(_health <= 0)
        {
            this.SetIsDestroyed(true);
        }
    }

    @Override
    public void Update()
    {
        _behavior.DoNextAction();
        super.Update();
    }

    public abstract void Attack();

    public abstract void Jump();
}
