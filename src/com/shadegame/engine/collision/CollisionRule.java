package com.shadegame.engine.collision;

/**
 * Created by Maashes on 4/12/2016.
 */
public class CollisionRule
{

    public static boolean CanCollide(CollisionType typeA, CollisionType typeB)
    {
        switch(typeA)
        {
            case ENEMY:
            case ENEMYPROJECTILE:
                if(typeB.equals(CollisionType.ENEMYPROJECTILE) || typeB.equals(CollisionType.ENEMY))
                {
                    return false;
                }
                break;

            case PLAYER:
            case PLAYERPROJECTILE:
                if(typeB.equals(CollisionType.PLAYER) || typeB.equals(CollisionType.PLAYERPROJECTILE))
                {
                    return false;
                }
                break;

            case GROUND:
            case ENVIRONMENT:
                return true;
        }

        return true;
    }



}
