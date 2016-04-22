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
    private PImage backImg;
    private PImage foreImg;

    public Stage(int width, int height, String bgImage, String foregroundImage, PApplet parent)
    {
        _width = width;
        _height = height;
        _sketchParent = parent;
        backImg = _sketchParent.loadImage(bgImage);
        foreImg = _sketchParent.loadImage(foregroundImage);
    }

    public Stage(int width, int height, String bgImage, PApplet parent)
    {
        _width = width;
        _height = height;
        _sketchParent = parent;
        backImg = _sketchParent.loadImage(bgImage);
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
        float xTranslation = EngineProvider.GetDefaultEngineInstance().GetXTranslation();
        float yTranslation = EngineProvider.GetDefaultEngineInstance().GetYTranslation();
        float maxXTranslation = EngineProvider.GetDefaultEngineInstance().GetMaxXTranslation();
        
        if(xTranslation < 0 && xTranslation >= maxXTranslation)
        {
            float xVelocity = EngineProvider.GetDefaultEngineInstance().GetPlayerVelocity().x;
            if(xVelocity >= 1)
            {
                _bgXTranslation-=1;

            }
            else if(xVelocity <= -1)
            {
                _bgXTranslation+=1;
            }
        }

        _sketchParent.image(backImg, xTranslation,yTranslation, _width, _height);

        if(foreImg != null)
        {
            _sketchParent.image(foreImg, xTranslation, yTranslation, _width,_height);
        }


    }

}
