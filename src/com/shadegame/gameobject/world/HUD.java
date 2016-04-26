package com.shadegame.gameobject.world;

import com.shadegame.engine.EngineProvider;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;

import static processing.core.PConstants.RECT;


/**
 * Created by Maashes on 4/17/2016.
 */
public class HUD
{
    private PFont _stageScore;
    private PImage _healthBarImage, _healthBarBorder;
    private PApplet _sketchParent;
    private float _currentPlayerHealth;
    private int _currentScore, _maxPlayerHealth;
    private String _score;

    public HUD(int currentHealth, int currentStageScore, PApplet parent)
    {
        _maxPlayerHealth = currentHealth;
        _sketchParent = parent;
        _currentScore = currentStageScore;
        _stageScore = _sketchParent.createFont("Arial",16,true);
        _healthBarImage = _sketchParent.loadImage("HUD/healthbarInner.png");
        _healthBarBorder = _sketchParent.loadImage("HUD/healthbarOuter.png");
    }

    public void UpdateHUD(int health, int score)
    {
        _currentScore = score;
        float currentHealth = ((float)health/_maxPlayerHealth)*300;
        _currentPlayerHealth = currentHealth;
        _score = Double.toString(_currentPlayerHealth);
    }

    public void DisplayHUD()
    {
        float xTranslation = -(EngineProvider.GetDefaultEngineInstance().GetXTranslation()-20);
        float yTranslation = -(EngineProvider.GetDefaultEngineInstance().GetYTranslation()-20);
        _sketchParent.image(_healthBarBorder, xTranslation, yTranslation,300,20);
        int currentBarWidth = (int)_currentPlayerHealth;
        PImage bar = _healthBarImage.get(0,0,currentBarWidth,20);
        _sketchParent.image(bar,xTranslation,yTranslation,_currentPlayerHealth,20);
        _sketchParent.textFont(_stageScore,16);
        _sketchParent.text(_score,xTranslation+200,yTranslation);
    }


}
