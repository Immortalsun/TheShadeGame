package com.shadowgame.engine.behavior;

import com.shadowgame.engine.EngineProvider;
import com.shadowgame.gameobject.enemy.Enemy;
import processing.core.PVector;

/**
 * Created by Maashes on 4/10/2016.
 */
public class RangedEnemyBehavior extends Behavior
{
    public RangedEnemyBehavior(Enemy enemy) {
        super(enemy);
    }

    @Override
    public void DoNextAction()
    {
        Enemy e = (Enemy)object;
        PVector playerLoc = EngineProvider.GetDefaultEngineInstance().GetPlayerLocation();
        float distToPlayer = Math.abs(playerLoc.x - e.GetLocation().x);
        if(distToPlayer > 299)
        {
            if(playerLoc.x > e.GetLocation().x)
            {
                e.GetVelocity().x = 1.0f;
            }
            else
            {
                e.GetVelocity().x = -1.0f;
            }
        }
        else if(distToPlayer < 290)
        {
            if(playerLoc.x > e.GetLocation().x)
            {
                e.GetVelocity().x = -1.0f;
            }
            else
            {
                e.GetVelocity().x = 1.0f;
            }
        }
        else if(distToPlayer > 290 && distToPlayer < 299)
        {
                e.GetVelocity().x = 0f;
                e.Attack();
        }
    }
}
