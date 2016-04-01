package Engine;

/**
 * Created by Maashes on 3/30/2016.
 */
import GameObject.GameObject;
import processing.core.PVector;
public class CollisionResult {

    public GameObject ObjectA;
    public GameObject ObjectB;
    public float PenetrationDepth;

    public CollisionDirection Direction;

    public CollisionResult(GameObject objectA, GameObject objectB, float depth)
    {
        ObjectA = objectA;
        ObjectB = objectB;

        PenetrationDepth = depth;
    }

    public CollisionResult()
    {
        ObjectA = null;
        ObjectB = null;

        PenetrationDepth = 0;

        Direction = CollisionDirection.NONE;
    }

}
