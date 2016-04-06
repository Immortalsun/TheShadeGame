package GameObject.Projectiles;

import GameObject.GameObject;
import processing.core.PApplet;

/**
 * Created by Maashes on 4/4/2016.
 */
public class Projectile extends GameObject {

    private int damage;
    private float flightSpeed;
    private float range;

    public Projectile(float x, float y, float objectWidth, float objectHeight, int damage, float speed, float range, int direction,PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        this.damage = damage;
        this.flightSpeed = speed;
        this.range = range;

        if(direction > 0)
        {
            this.GetVelocity().x += flightSpeed;
        }
        else
        {
            this.GetVelocity().x -= flightSpeed;
        }

    }

    public int GetDamage()
    {
        return damage;
    }

    public float GetFlightSpeed()
    {
        return flightSpeed;
    }

    public float GetRange()
    {
        return range;
    }
}