package com.shadegame.gameobject.world;

import com.shadegame.engine.EngineProvider;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Created by Maashes on 4/6/2016.
 */

public class Stage
{
    private int _width, _height, _score,_bgXTranslation;
    private PApplet _sketchParent;
    private String _platformFile;
    private PImage backImg, lastBackImg;
    private PImage foreImg, lastForeImg;

    public Stage(int width, int height, String bgImage, String foregroundImage, String level ,PApplet parent)
    {
        _width = width;
        _height = height;
        _sketchParent = parent;
        _platformFile = level;
    }

    public Stage(PApplet parent)
    {
        _sketchParent = parent;
    }

    public void LoadImages(String[] images)
    {
        backImg = _sketchParent.loadImage(images[0]);
        foreImg = _sketchParent.loadImage(images[1]);
    }

    public int GetWidth()
    {
        return _width;
    }

    public int GetHeight()
    {
        return _height;
    }

    public int GetScore(){ return _score; }

    public void SetWidth(int width)
    {
        _width = width;
    }

    public void SetHeight(int height)
    {
        _height = height;
    }

    public String GetLevelFile(){return _platformFile;}

    public void IncrementScore(int scoreInc)
    {
        _score+=scoreInc;
    }

    public void DecrementScore(int scoreDec)
    {
        if(_score - scoreDec >= 0)
        {
            _score-=scoreDec;
        }
        else{
            _score=0;
        }
    }

    public void DisplayStage()
    {
        int screenWidth = EngineProvider.GetDefaultEngineInstance().GetScreenWidth();
        int screenHeight = EngineProvider.GetDefaultEngineInstance().GetScreenHeight();

        if(EngineProvider.GetDefaultEngineInstance().GetIsPaused())
        {
            _sketchParent.image(lastBackImg,0,0, screenWidth, screenHeight);
            if(foreImg != null)
            {
                _sketchParent.image(lastForeImg, 0, 0, screenWidth,screenHeight);
            }
            return;
        }

        float xTranslation = EngineProvider.GetDefaultEngineInstance().GetXTranslation();
        float yTranslation = EngineProvider.GetDefaultEngineInstance().GetYTranslation();
        float maxXTranslation = EngineProvider.GetDefaultEngineInstance().GetMaxXTranslation();
        
        if(xTranslation < 0 && xTranslation >= maxXTranslation)
        {
            float xVelocity = EngineProvider.GetDefaultEngineInstance().GetPlayerVelocity().x;
            if(xVelocity >= 1)
            {
                _bgXTranslation+=1;

            }
            else if(xVelocity <= -1)
            {
                _bgXTranslation-=1;
            }
        }
        int yLoc = -(int)yTranslation;
        int xLoc = -(int)xTranslation;

        PImage bgFrame = backImg.get(_bgXTranslation,yLoc,screenWidth, screenHeight);
        _sketchParent.image(bgFrame,0,0, screenWidth, screenHeight);

        if(foreImg != null)
        {
            PImage fgFrame = foreImg.get(xLoc,yLoc,screenWidth,screenHeight);
            _sketchParent.image(fgFrame, 0, 0, screenWidth,screenHeight);
            lastForeImg = fgFrame;
        }

        lastBackImg = bgFrame;

    }

}
