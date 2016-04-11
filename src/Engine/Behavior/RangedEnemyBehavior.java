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
    private int _attackCounter,_maxAttacksPerSecond;
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
            _attackCounter = 0;
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
            _attackCounter = 0;
        }
        else if(distToPlayer > 290 && distToPlayer < 299)
        {
                e.GetVelocity().x = 0f;
                e.Attack();
        }
    }
}
