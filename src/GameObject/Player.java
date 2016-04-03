package GameObject;

import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        BuildAnimator("player1.png", "player1Reversed.png", 3, 10);
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
