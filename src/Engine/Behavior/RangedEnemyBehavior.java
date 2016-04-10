package Engine.Behavior;

import Engine.EngineProvider;
import GameObject.Enemy.Enemy;
import GameObject.GameObject;
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
        if(distToPlayer > 300)
        {
            if(playerLoc.x > e.GetLocation().x)
            {
                e.GetVelocity().x = -2.5f;
            }
            else
            {
                e.GetVelocity().x = 2.5f;
            }
        }
        else if(distToPlayer < 300)
        {
            if(playerLoc.x > e.GetLocation().x)
            {
                e.GetVelocity().x = 2.5f;
            }
            else
            {
                e.GetVelocity().x = -2.5f;
            }
        }
        else
        {
            e.Attack();
        }
    }
}
