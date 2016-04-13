package com.shadegame.engine;

import com.shadegame.engine.behavior.RangedEnemyBehavior;
import com.shadegame.gameobject.enemy.ButtEnemy;
import com.shadegame.gameobject.enemy.Enemy;
import com.shadegame.gameobject.enemy.EnemyType;

/**
 * Created by Maashes on 4/10/2016.
 */
public class Spawner
{
    private int _maxEnemyCount, _currentEnemyCount, _spawnRate, _rateCounter;
    private float x, y;
    private String _id;
    private Enemy _currentEnemy;
    private EnemyType _spawnType;

    public Spawner(float xLoc, float yLoc, EnemyType type, String id, int rate, int maxEnemies)
    {
        x= xLoc;
        y = yLoc;
        _maxEnemyCount = maxEnemies;
        _spawnType = type;
        _spawnRate = rate;
        _rateCounter = 1;
        _id = id;
    }

    public boolean Spawn()
    {
        if(_currentEnemyCount < _maxEnemyCount)
        {
            if(_rateCounter % _spawnRate == 0)
            {
                CreateEnemy();
                _rateCounter = 1;
                return true;
            }
            else
            {
                _rateCounter++;
            }
        }
        return false;

    }

    private void CreateEnemy()
    {
        _currentEnemy = SpawnUtility.GetEnemyFromType(this._spawnType, x,y, _id,10);
        _currentEnemyCount++;
    }

    public Enemy GetCurrentEnemy()
    {
        return _currentEnemy;
    }

    public void ClearCurrentEnemy()
    {
        _currentEnemy = null;
    }

    public void EnemyDestroyed(String spawnID)
    {
        if(_id.equals(spawnID))
        {
            _currentEnemyCount --;
        }
    }

    private static class SpawnUtility
    {
        public static Enemy GetEnemyFromType(EnemyType type, float xLoc, float yLoc, String id, int health)
        {
            switch(type)
            {
                case RANGED:
                    ButtEnemy buttEnemy = new ButtEnemy(xLoc,yLoc, 32,32, EngineProvider.GetDefaultEngineInstance().GetSketchParent(), id, health);
                    buttEnemy.SetBehavior(new RangedEnemyBehavior(buttEnemy));
                    return buttEnemy;

                default:
                    return new ButtEnemy(xLoc,yLoc, 32,32, EngineProvider.GetDefaultEngineInstance().GetSketchParent(), id, health);
            }
        }
    }
}
