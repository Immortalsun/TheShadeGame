package GameObject;

import processing.core.*;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        Animation[] animations = new Animation[2];
        animations[0] = new Animation(parent,"player1.png", "player1Reversed.png",AnimationState.RUNNING,3, 10);
        animations[1] = new Animation(parent,"playerJump.png", "playerJumpReversed.png", AnimationState.JUMPING,2,10);
        BuildAnimator(animations);
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
