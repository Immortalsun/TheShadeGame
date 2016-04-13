package com.shadowgame.engine;

/**
 * Created by Maashes on 3/30/2016.
 */
import com.shadowgame.engine.collision.CollisionDirection;
import com.shadowgame.engine.collision.CollisionResult;
import com.shadowgame.engine.collision.CollisionRule;
import com.shadowgame.engine.collision.CollisionType;
import com.shadowgame.gameobject.*;
import com.shadowgame.gameobject.enemy.ButtEnemy;
import com.shadowgame.gameobject.enemy.Enemy;
import com.shadowgame.gameobject.enemy.EnemyType;
import com.shadowgame.gameobject.projectiles.Projectile;
import com.shadowgame.gameobject.world.Stage;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine
{
    private int screenWidth;
    private int screenHeight;
    private float xTranslation, yTranslation, baseYTanslation, maxXTranslation;
    private ArrayList<GameObject> _gameObjectCollection;
    private ArrayList<Enemy> _enemyColection;
    private HashMap<String, Spawner> _spawnMap;
    private Stage _currentStage;
    private GameObject groundLevel;
    private Player player;
    private PApplet sketchParent;

    private final float gravityConstant = .5f;

    public Engine(int scrWidth, int scrHeight, PApplet parent, Stage s)
    {
        _currentStage = s;
        screenWidth = scrWidth;
        screenHeight = scrHeight;
        _gameObjectCollection = new ArrayList(1);
        _enemyColection = new ArrayList<Enemy>(1);
        _spawnMap = new HashMap<String, Spawner>();
        sketchParent = parent;
        EngineProvider.SetDefaultEngineInstance(this);
    }

    public Player CretePlayer(float x, float y, float objectWidth, float objectHeight)
    {
        player = new Player(x,y, objectWidth, objectHeight, this.sketchParent);
        player.SetIsPlayer(true);
        yTranslation = baseYTanslation = (screenHeight/1.06f) - player.GetLocation().y;
        maxXTranslation = -(screenWidth);

        return player;
    }

    public void SetLevelBounds()
    {
        groundLevel = new GameObject(0,_currentStage.GetHeight()-15, _currentStage.GetWidth()-1, 15, this.sketchParent);
        groundLevel.SetIsGround(true);
        groundLevel.SetCollisionType(CollisionType.GROUND);
        _gameObjectCollection.add(groundLevel);

        GameObject leftBound = new GameObject(-4,0, 5,_currentStage.GetHeight()-1, this.sketchParent);
        leftBound.SetIsGround(true);
        leftBound.SetCollisionType(CollisionType.GROUND);
        _gameObjectCollection.add(leftBound);

        GameObject rightBound = new GameObject(_currentStage.GetWidth()-1, 0, 5,_currentStage.GetHeight()-1, this.sketchParent);
        rightBound.SetCollisionType(CollisionType.GROUND);
        rightBound.SetIsGround(true);
        _gameObjectCollection.add(rightBound);
    }

    public void GeneratePlatforms()
    {
        float startX = 500;
        float startY = groundLevel.GetMinY() - 105;
        for(int i = 0; i < 10; i++)
        {
            if(i % 2 == 0)
            {
                GameObject platform = new GameObject(startX, startY, 50, 15, this.sketchParent);
                platform.SetIsGround(true);
                platform.SetCollisionType(CollisionType.GROUND);
                _gameObjectCollection.add(platform);


            }
            startX += 70;
            startY -= 35;
        }
    }

    public void PlaceSpawners()
    {
        _spawnMap.put("s1", new Spawner(500, groundLevel.GetMinY()-32, EnemyType.RANGED, "s1", 1, 1));
    }

    public void Update()
    {
        CleanupDestroyedObjects();

        UpdatePlayer();

        UpdateEnemies();

        for(GameObject g : _gameObjectCollection)
        {
            if(g instanceof Projectile)
            {
                ArrayList<CollisionResult> projectileCollisions = CheckCollisions(g);

                if(!projectileCollisions.isEmpty())
                {
                    g.SetIsDestroyed(true);
                }
                else
                {
                    ((Projectile) g).CheckRange();
                }
            }
            g.Update();
        }

        SpawnEnemies();
    }

    private void UpdatePlayer()
    {
        player.Update();
        //check to see if player has any collisions
        ArrayList<CollisionResult> collisions = CheckCollisions(player);
        if(!collisions.isEmpty())
        {
            //if we have any, resolve them and adjust player location and velocity
            for(CollisionResult result : collisions)
            {
                ResolveCollision(result);
            }
        }

        if(player.GetVelocity().y < 10)
        {
            player.GetVelocity().y+=gravityConstant;
        }

        if(player.GetIsOnGround())
        {
            player.SetIsJumping(false);
        }

        player.SetIsOnGround(CheckOnGround(player));

        if(player.GetHasCastProjectile())
        {
            _gameObjectCollection.add(player.GetCurrentProjectile());
            player.ClearCurrentProjectile();
        }
    }

    private void UpdateEnemies()
    {
        if(!_enemyColection.isEmpty())
        {
            for(Enemy enemy : _enemyColection)
            {
                enemy.Update();

                if(enemy.GetVelocity().y < 10)
                {
                    enemy.GetVelocity().y+=gravityConstant;
                }

                enemy.SetIsOnGround(CheckOnGround(enemy));

                ArrayList<CollisionResult> collisions = CheckCollisions(enemy);

                if(!collisions.isEmpty())
                {
                    for (CollisionResult coll : collisions)
                    {
                        ResolveCollision(coll);

                        if(coll.ObjectB instanceof Projectile)
                        {
                            Projectile proj = (Projectile)coll.ObjectB;
                            enemy.TakeDamage(proj.GetDamage());
                            proj.SetIsDestroyed(true);
                        }
                    }
                }


                if(enemy.GetIsAttacking() && enemy.GetType().equals(EnemyType.RANGED))
                {
                    ButtEnemy buttEnemy = (ButtEnemy)enemy;
                    _gameObjectCollection.add(buttEnemy.GetCurrentProjectile());
                    buttEnemy.ClearCurrentProjectile();
                    buttEnemy.SetIsAttacking(false);
                }

                if(enemy.GetIsDestroyed())
                {
                    enemy.SetIsReadyForCleanup(true);
                }
            }
        }
    }

    private ArrayList<CollisionResult> CheckCollisions(GameObject obj)
    {
        ArrayList<CollisionResult> results = new ArrayList<CollisionResult>();
        for(GameObject g : _gameObjectCollection)
        {
            if(!obj.equals(g))
            {
                if(CheckCollision(obj, g))
                {
                    results.add(GetCollisionResult(obj,g));
                }
            }

        }
        return results;
    }



    public void Display()
    {
        KeepPlayerInViewport();

        player.Display();

        for(Enemy e : _enemyColection)
        {
            e.Display();
        }

        for(GameObject g : _gameObjectCollection)
        {
            g.Display();
        }
    }

    public void SpawnEnemies()
    {
        for(Spawner s : _spawnMap.values()){
            boolean spawned = s.Spawn();
            if(spawned)
            {
                Enemy e = s.GetCurrentEnemy();
                if(e != null)
                {
                    _enemyColection.add(e);
                    s.ClearCurrentEnemy();
                }
            }
        }
    }

    private void DespawnEnemies(Enemy e)
    {
        Spawner spawner = _spawnMap.get(e.GetSpawnID());
        if(spawner != null)
        {
            spawner.EnemyDestroyed(e.GetSpawnID());
        }
    }

    public static boolean CheckCollision(GameObject obj1, GameObject obj2)
    {
        if(!CollisionRule.CanCollide(obj1.GetCollisionType(), obj2.GetCollisionType())) return false;

        if(obj1.GetMaxX() < obj2.GetMinX() || obj1.GetMinX() > obj2.GetMaxX()) return false;

        if(obj1.GetMaxY() < obj2.GetMinY() || obj1.GetMinY() > obj2.GetMaxY()) return false;

        return true;
    }

    public static CollisionResult GetCollisionResult(GameObject objA, GameObject objB)
    {
        CollisionResult result = new CollisionResult();
        result.ObjectA = objA;
        result.ObjectB = objB;

        //find intersecting rectangle
        float xLeft = Math.max(objA.GetMinX(), objB.GetMinX());
        float yTop = Math.max(objA.GetMinY(), objB.GetMinY());
        float xRight = Math.min(objA.GetMaxX(), objB.GetMaxX());
        float yBottom = Math.min(objA.GetMaxY(), objB.GetMaxY());

        //overlap on x axis is width of overlapping rectangle
        float xOverlap = (xRight-xLeft);
        //overlap on y axis is height of overlapping rectangle
        float yOverlap = (yBottom-yTop);

        //Get vector from A to B
        PVector objALocation = new PVector(objA.GetMinX(), objA.GetMinY());
        PVector objBLocation = new PVector(objB.GetMinX(), objB.GetMinY());
        PVector locationVector = new PVector((objBLocation.x - objALocation.x),(objBLocation.y - objALocation.y));

        //if we have some overlapping distance
        if(xOverlap > 0 || yOverlap > 0)
        {
            //we want to choose overlap that is smallest
            if(xOverlap < yOverlap)
            {
                if(locationVector.x > 0)
                {
                    result.Direction = CollisionDirection.FROMLEFT;
                }
                else
                {
                    result.Direction = CollisionDirection.FROMRIGHT;
                }
                result.PenetrationDepth = xOverlap;
                return result;
            }
            else
            {
                if(locationVector.y < 0)
                {
                    result.Direction = CollisionDirection.FROMBELOW;
                }
                else
                {
                    result.Direction = CollisionDirection.FROMABOVE;
                }
                result.PenetrationDepth = yOverlap;
                return result;
            }
        }

        return new CollisionResult();
    }

    public boolean CheckOnGround(GameObject obj)
    {
        for(GameObject object : _gameObjectCollection)
        {
            if(object.GetIsGround())
            {
                boolean collidingWithGround = CheckCollision(obj, object);

                if(collidingWithGround)
                {
                    CollisionResult result = GetCollisionResult(obj, object);

                    if(result.Direction.equals(CollisionDirection.FROMABOVE))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void ResolveCollision(CollisionResult result)
    {
        switch (result.Direction)
        {
            case FROMABOVE:
                //if we are colliding from above
                //if both objects are not ground objects, we want to move them in opposite directions
                //each half of the total penetration distance. In this case, the -y direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().y += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().y = 0;
                    result.ObjectB.GetVelocity().y = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= result.PenetrationDepth;
                    result.ObjectA.GetVelocity().y = 0;
                }
                break;
            case FROMBELOW:
                //colliding from below is like coliding from above except we need to move things in the positive y direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().y += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().y = 0;
                    result.ObjectB.GetVelocity().y = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().y += result.PenetrationDepth;
                    result.ObjectA.GetVelocity().y = 0;
                }
                break;
            case FROMLEFT:
                //Colliding from the left involves the x axis, we need to move in the -x direction to resolve the collision
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x += (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().x -= (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().x = 0;
                    result.ObjectB.GetVelocity().x = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x -= result.PenetrationDepth;
                    result.ObjectA.GetVelocity().x = 0;
                }
                break;
            case FROMRIGHT:
                //colliding from the right means we need to move in the +x direction
                if(!result.ObjectA.GetIsGround() && !result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x -= (result.PenetrationDepth/2);
                    result.ObjectB.GetLocation().x += (result.PenetrationDepth/2);

                    result.ObjectA.GetVelocity().x = 0;
                    result.ObjectB.GetVelocity().x = 0;
                }
                //if one object is a ground object and the other is not, move the non-ground object in the -y
                //direction by the penetration distance
                else if(!result.ObjectA.GetIsGround() && result.ObjectB.GetIsGround())
                {
                    result.ObjectA.GetLocation().x += result.PenetrationDepth;
                    result.ObjectA.GetVelocity().x = 0;
                }
                break;
        }
    }

    private void CleanupDestroyedObjects()
    {
        ArrayList<GameObject> collection = new ArrayList<GameObject>(_gameObjectCollection);

        for(GameObject g : collection)
        {
            if((g.GetIsReadyForCleanup()) && _gameObjectCollection.contains(g))
            {
                _gameObjectCollection.remove(g);
            }
        }

        ArrayList<Enemy> enemies = new ArrayList<Enemy>(_enemyColection);

        for(Enemy e : enemies)
        {
            if(e.GetIsReadyForCleanup() && _enemyColection.contains(e))
            {
                _enemyColection.remove(e);
                DespawnEnemies(e);
            }
        }
    }

    private void KeepPlayerInViewport()
    {
        if(player.GetLocation().x > (screenWidth/2) && xTranslation >= maxXTranslation)
        {
            xTranslation = (screenWidth/2) - player.GetLocation().x;
        }
        else if(player.GetLocation().x+xTranslation < (screenWidth/2) && xTranslation < 0)
        {
            xTranslation = (screenWidth/2) - player.GetLocation().x;
        }

       if(player.GetLocation().y+yTranslation < (screenHeight/2) && yTranslation < -5)
       {
           yTranslation = (screenHeight/2) - player.GetLocation().y;
       }
       else if((player.GetLocation().y+yTranslation > (screenHeight/2)) && yTranslation > baseYTanslation)
       {
           yTranslation = ((screenHeight/2) - player.GetLocation().y);
       }

        sketchParent.translate(xTranslation, yTranslation);
    }

    public PVector GetPlayerLocation()
    {
        return new PVector(player.GetLocation().x, player.GetLocation().y);
    }

    public int GetPlayerOrientation() {return player.GetOrientation();}

    public PApplet GetSketchParent()
    {
        return sketchParent;
    }

    public int GetXTranslation()
    {
        return (int)xTranslation;
    }

    public int GetYTranslation()
    {
        return (int)yTranslation;
    }
}
