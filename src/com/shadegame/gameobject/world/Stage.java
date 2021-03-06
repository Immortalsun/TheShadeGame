package com.shadegame.gameobject.world;

import com.shadegame.engine.EngineProvider;
import com.shadegame.engine.utils.Platform;
import com.shadegame.engine.utils.PlatformGroup;
import com.shadegame.gameobject.GameObject;
import com.shadegame.gameobject.animation.Animation;
import com.shadegame.gameobject.animation.AnimationState;
import com.shadegame.gameobject.animation.TextureAnimation;
import processing.awt.PGraphicsJava2D;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static processing.core.PConstants.JAVA2D;

/**
 * Created by Maashes on 4/6/2016.
 */

public class Stage
{
    private int _width, _height, _score,_bgXTranslation;
    private PApplet _sketchParent;
    private PImage backImg, lastBackImg;
    private PImage foreImg, lastForeImg;

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

    public void SkinPlatform(GameObject platform, String imgPath, float animX, float animY)
    {
        if(platform != null && imgPath != null)
        {
            PImage platformImage = _sketchParent.loadImage(imgPath);
            Animation[] textureAnims = new Animation[1];
            TextureAnimation textureAnim = new TextureAnimation(platformImage);
            textureAnim.SetAnimationLocation(animX,animY);
            textureAnims[0] = textureAnim;

            platform.BuildAnimator(textureAnims);
        }
    }

    public void SkinPlatformGroup(GameObject platform, String[] imagePaths, ArrayList<Platform> groupChildren, float animX, float animY)
    {
        if(platform != null && imagePaths.length > 0)
        {
            int platWidth = (int)platform.GetWidth();
            int platHeight = (int)platform.GetHeight();
            float imgX = 0;
            float imgY = 0;
            Platform prevChild = null;
            PGraphics combinedSkin = _sketchParent.createGraphics(platWidth,platHeight, JAVA2D);
            for(int i=0; i<imagePaths.length; i++)
            {
                if(prevChild != null)
                {
                    if(groupChildren.get(i).XLoc > prevChild.XLoc)
                    {
                        imgX+=groupChildren.get(i).Width;
                    }
                    if(groupChildren.get(i).YLoc > prevChild.YLoc)
                    {
                        imgY += groupChildren.get(i).Height;
                    }
                }
                PImage imageFragment = _sketchParent.loadImage(imagePaths[i]);
                combinedSkin.beginDraw();
                combinedSkin.image(imageFragment,imgX,imgY);
                prevChild = groupChildren.get(i);
            }
            combinedSkin.endDraw();
            PImage platformImage = combinedSkin.get();
            Animation[] textureAnims = new Animation[1];
            TextureAnimation textureAnim = new TextureAnimation(platformImage);
            textureAnim.SetAnimationLocation(animX,animY);
            textureAnims[0] = textureAnim;

            platform.BuildAnimator(textureAnims);
        }
    }

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
