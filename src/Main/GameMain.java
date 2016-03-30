package Main; /**
 * Created by Maashes on 3/30/2016.
 */

import processing.core.*;
import GameObject.*;
import Engine.*;
import java.util.HashMap;
import java.util.Map;

public class GameMain extends PApplet
{
    float acceleration = 0.1f;
    int windowWidth = 800;
    int windowHeight = 400;
    boolean isKeyPressed;
    HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    PVector playerVelocity = new PVector(0,0);
    PFont f;
    Engine engine;
    Player player;
    int currentDir;

    public static void main(String[] args) {
        PApplet.main(GameMain.class.getName());
    }

    public void settings()
    {
        size(800,400);
    }

    public void setup()
    {
        f = createFont("Arial",16,true);
        keyMap.put(37, false);
        keyMap.put(38, false);
        keyMap.put(39, false);
        engine = new Engine(windowWidth, windowHeight, this);
        player = engine.CretePlayer(0,0, 20,20);
        player.SetIsJumping(true);
        engine.SetGroundLevel();
    }

    public void draw()
    {
        background(103, 102);
        textFont(f,16);
        fill(255);
        text("("+mouseX+", "+mouseY+")",mouseX,mouseY);
        CheckKeyStatus();
        engine.Update();
        engine.Display();
    }

    public void keyPressed()
    {
        if(key == CODED)
        {
            if(keyCode == 37)
            {
                currentDir = 3;
                keyMap.replace(37, true);
            }
            else if(keyCode == 39)
            {
                currentDir = 4;
                keyMap.replace(39, true);
            }
            else if(keyCode == 38)
            {
                currentDir = 5;
                keyMap.replace(38, true);
            }
        }
    }

    public void keyReleased()
    {
        if(key == CODED)
        {
            if(keyCode == 37)
            {
                currentDir = 3;
                keyMap.replace(37, false);
            }
            else if(keyCode == 39)
            {
                currentDir = 4;
                keyMap.replace(39, false);
            }
            else if(keyCode == 38)
            {
                currentDir = 5;
                keyMap.replace(38, false);
            }
        }
    }

    public void CheckKeyStatus()
    {
        isKeyPressed = CheckIsKeyPressed();
        if(isKeyPressed)
        {
            Accelerate();
        }
        else
        {
            Decelerate();
        }
    }

    public void MovePlayer()
    {
        if(isKeyPressed)
        {
            switch(currentDir)
            {
                //3 is coded to left
                case 3:
                    if(playerVelocity.x > -2.5)
                    {
                        playerVelocity.x -= (acceleration);
                    }
                    break;
                //4 is coded to right
                case 4:
                    if(playerVelocity.x < 2.5)
                    {
                        playerVelocity.x += (acceleration);
                    }
                    break;
                //5 is a jump
                case 5:
                    if(!player.GetIsJumping() && (boolean)keyMap.get(38))
                    {
                        playerVelocity.y -= 15;
                        player.SetIsJumping(true);
                        player.SetIsOnGround(false);
                    }
                    break;
            }
        }
        player.Move(playerVelocity);
    }

    public void Accelerate()
    {
        MovePlayer();
    }

    public void Decelerate()
    {
        if((playerVelocity.x >= 0 && playerVelocity.x <= acceleration) ||
                (playerVelocity.x < 0 && playerVelocity.x >= (acceleration * -1)))
        {
            playerVelocity.x = 0;
        }
        else if(playerVelocity.x > 0)
        {
            playerVelocity.x -= acceleration;
        }
        else if (playerVelocity.x < 0)
        {
            playerVelocity.x += acceleration;
        }

        MovePlayer();
    }

    public boolean CheckIsKeyPressed()
    {
        for(Map.Entry entry : keyMap.entrySet())
        {
            Boolean isPressed = (Boolean)entry.getValue();
            if(isPressed)
            {
                return true;
            }
        }

        return false;
    }
}
