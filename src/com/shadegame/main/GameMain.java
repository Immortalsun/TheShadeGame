package com.shadegame.main;
/**
 * Created by Maashes on 3/30/2016.
 */

import com.shadegame.gameobject.player.Player;
import com.shadegame.gameobject.world.HUD;
import com.shadegame.gameobject.world.Stage;
import javafx.scene.input.KeyCode;
import processing.core.*;
import com.shadegame.engine.*;
import java.util.HashMap;
import java.util.Map;

public class GameMain extends PApplet
{
    float acceleration = 0.1f;
    int windowWidth = 800;
    int windowHeight = 500;
    int stageWidth, stageHeight;
    boolean isKeyPressed, paused, attacking, charging;
    HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    PVector playerVelocity = new PVector(0,0);
    Engine engine;
    Player player;

    public static void main(String[] args) {
        PApplet.main(GameMain.class.getName());
    }

    public void settings()
    {
        size(800,500);
    }

    public void setup()
    {
        keyMap.put(37, false);
        keyMap.put(38, false);
        keyMap.put(39, false);
        keyMap.put(32, false);
        engine = new Engine(windowWidth, windowHeight, this);
        stageWidth = engine.GetStageWidth();
        stageHeight = engine.GetStageHeight();
        player = engine.GetPlayer();
    }

    public void draw()
    {
        CheckKeyStatus();
        engine.Update();
        engine.Display();
    }

    public void keyPressed()
    {
        if(key == CODED)
        {
            if(paused)
                return;
            if(keyCode == 37)
            {
                keyMap.replace(37, true);
            }
            else if(keyCode == 39)
            {
                keyMap.replace(39, true);
            }
            else if(keyCode == 38)
            {
                keyMap.replace(38, true);
            }
        }
        else
        {
            if(key=='d' || key=='D')
            {
                ToggleDebugMode();
            }

            if (key == 'P' || key == 'p')
            {
                Pause();
            }
            else if(key == 32 || key == ' ')
            {
                if(paused)
                    return;
                keyMap.replace(32, true);
               Attack();
            }
            else if(key>=49 && key <=54)
            {
                if(paused)
                    return;
                if(!charging) {
                    player.Charge(((int)key-48));
                    charging = true;
                }
            }
        }
    }

    public void keyReleased()
    {
        if(key == CODED)
        {
            if(keyCode == 37)
            {
                keyMap.replace(37, false);
            }
            else if(keyCode == 39)
            {
                keyMap.replace(39, false);
            }
            else if(keyCode == 38)
            {
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
            else if((key>=49 && key <=54))
            {
                charging = false;
            }
        }

    }

    public void CheckKeyStatus()
    {
        if(paused)
            return;

        isKeyPressed = CheckIsKeyPressed();
        if(isKeyPressed)
        {
            MovePlayer();
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
            if(keyMap.get(37) && keyMap.get(39))
            {
                playerVelocity.x =0;
            }
            else if(keyMap.get(37))
            {
                playerVelocity.x = -2.5f;
            }
            else if(keyMap.get(39))
            {
                playerVelocity.x = 2.5f;
            }

            if(keyMap.get(38) && !player.GetIsJumping())
            {
                player.Jump();
                player.SetIsJumping(true);
                player.SetIsOnGround(false);
            }
        }
        player.Move(playerVelocity);
    }

    public void Attack() {
        if (paused)
            return;

        if (!attacking) {
            attacking = true;
            player.CastProjectile();
        }
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
            engine.PauseEngine();
        }
        else
        {
            paused = false;
            engine.PauseEngine();
        }
    }

    public void ToggleDebugMode()
    {
        engine.ToggleEngineDebugMode();
    }
}
