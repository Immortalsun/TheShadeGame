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
    float playerSpeed = 2.8f;
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
        keyMap.put(37, false); //left arrow. movement
        keyMap.put(38, false); //up arrow jump
        keyMap.put(39, false); //right arrow, movement
        keyMap.put(32, false); //space for attack, unmodified does projectile
        keyMap.put(113, false); //Q attack modifier for melee
        keyMap.put(119, false); //W attack modifier for AOE
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
            else if(key == 113)
            {
                if(paused)
                    return;
                keyMap.replace(113, true);
            }
            else if(key == 'w')
            {
                if(paused)
                    return;
                keyMap.replace(119, true);
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
            else if(key == 113)
            {
                keyMap.replace(113, false);
            }
            else if(key == 119)
            {
                if(paused)
                    return;
                keyMap.replace(119, false);
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
                playerVelocity.x = -(playerSpeed);
            }
            else if(keyMap.get(39))
            {
                playerVelocity.x = playerSpeed;
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
            if((!keyMap.get(113) && !keyMap.get(119)) || (keyMap.get(113) && keyMap.get(119)))
            {
                player.CastProjectile();
            }
            else if(keyMap.get(113))
            {
                player.CastMeleeAttack();
            }
            else
            {
                //TODO cast AOE attacks
            }
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
