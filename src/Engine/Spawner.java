package Engine;

import GameObject.Enemy.Enemy;
import GameObject.Enemy.EnemyType;

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

    public Spawner(float xLoc, float yLoc, EnemyType type, String id)
    {
        x= xLoc;
        y = yLoc;
        _spawnType = type;
        _rateCounter = 1;
        _id = id;
    }

    public void Spawn()
    {
        if(_currentEnemyCount < _maxEnemyCount)
        {
            if(_rateCounter % _spawnRate == 0)
            {
                CreateEnemy();
            }
            else
            {
                _rateCounter++;
            }
        }

    }

    private void CreateEnemy()
    {
        _currentEnemy = new Enemy(x,y, 32,32, EngineProvider.GetDefaultEngineInstance().GetSketchParent(), _id, 10);
        _currentEnemyCount++;
    }

    private void EnemyDestroyed(String spawnID)
    {
        if(_id.equals(spawnID))
        {
            _currentEnemyCount --;
        }
    }
}
