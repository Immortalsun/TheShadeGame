package GameObject;

import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);
    }

    public void Move(PVector moveVector)
    {
        SetVelocity(moveVector);
    }
}
