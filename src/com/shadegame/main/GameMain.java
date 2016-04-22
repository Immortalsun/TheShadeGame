package com.shadegame.main; /**
 * Created by Maashes on 3/30/2016.
 */

import com.shadegame.gameobject.world.HUD;
import com.shadegame.gameobject.world.Stage;
import processing.core.*;
import com.shadegame.gameobject.*;
import com.shadegame.engine.*;
import java.util.HashMap;
import java.util.Map;

public class GameMain extends PApplet
{
    float acceleration = 0.1f;
    int windowWidth = 800;
    int windowHeight = 350;
    int stageWidth = 7554;
    int stageHeight = 500;
    boolean isKeyPressed, paused, attacking;
    HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    PVector playerVelocity = new PVector(0,0);
    Engine engine;
    Player player;
    Stage currentStage;
    HUD hud;
    int currentDir;

    public static void main(String[] args) {
        PApplet.main(GameMain.class.getName());
    }

    public void settings()
    {
        size(800,350);
    }

    public void setup()
    {
        keyMap.put(37, false);
        keyMap.put(38, false);
        keyMap.put(39, false);
        keyMap.put(32, false);
        currentStage = new Stage(stageWidth, stageHeight, "WorldSprites/marioEdit.png",this);
        engine = new Engine(windowWidth, windowHeight, this, currentStage);
        player = engine.CretePlayer(10,stageHeight-60, 32,32);
        player.SetIsJumping(true);
        engine.SetLevelBounds();
        engine.GeneratePlatforms();
        engine.PlaceSpawners();
        hud = new HUD(player.GetHealth(),currentStage.GetScore(),this);
    }

    public void draw()
    {
        //background(205);
        currentStage.DisplayStage();
        CheckKeyStatus();
        engine.Update();
        hud.UpdateHUD(player.GetHealth(),currentStage.GetScore());
        engine.Display();
        hud.DisplayHUD();
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
        else
        {
            if (key == 'P' || key == 'p')
            {
                Pause();
            }
            else if(key == 32 || key == ' ')
            {
                keyMap.replace(32, true);
               Attack();
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
        else
        {
            if(key == 32 || key == ' ')
            {
                keyMap.replace(32, false);
                attacking = false;
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
                    if(!player.GetIsJumping() && keyMap.get(38))
                    {
                        player.Jump();
                        player.SetIsJumping(true);
                        player.SetIsOnGround(false);
                    }
                    break;
            }
        }
        player.Move(playerVelocity);
    }

    public void Attack()
    {
        if(!attacking)
        {
            attacking = true;
            player.CastProjectile();
        }
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
            if((Integer)entry.getKey() != 32)
            {
                Boolean isPressed = (Boolean)entry.getValue();
                if(isPressed)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void Pause()
    {
        if(!paused)
        {
            paused = true;
            noLoop();
        }
        else
        {
            paused = false;
            loop();
        }
    }
}
