package GameObject;

import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);
        SetImage("playerTest1.png", 3, 10);
        SetFlippedImage("playerTest2.png");
    }

    public void Move(PVector moveVector)
    {
        SetVelocity(moveVector);
    }

    public void Jump()
    {
        GetVelocity().y -= 12;
    }
}
