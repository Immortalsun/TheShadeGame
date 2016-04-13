package com.shadowgame.engine.Collision;

/**
 * Created by Maashes on 4/12/2016.
 */
public class CollisionRule
{

    public static boolean CanCollide(CollisionType typeA, CollisionType typeB)
    {
        switch(typeA)
        {
            case ENEMYPROJECTILE:
                if(typeB.equals(CollisionType.ENEMYPROJECTILE) || typeB.equals(CollisionType.ENEMY))
                {
                    return false;
                }
                break;

            case PLAYERPROJECTILE:
                if(typeB.equals(CollisionType.PLAYER) || typeB.equals(CollisionType.PLAYERPROJECTILE))
                {
                    return false;
                }
                break;

            case ENEMY:
                if(typeB.equals(CollisionType.ENEMY) || typeB.equals(CollisionType.ENEMYPROJECTILE))
                {
                    return false;
                }
                break;

            case GROUND:
            case ENVIRONMENT:
                return true;

            case PLAYER:
                if(typeB.equals(CollisionType.PLAYER) || typeB.equals(CollisionType.PLAYERPROJECTILE))
                {
                    return false;
                }
                break;
        }

        return true;
    }



}
