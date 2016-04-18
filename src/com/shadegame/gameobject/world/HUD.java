package com.shadegame.gameobject.world;

import com.shadegame.engine.EngineProvider;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShape;

import static processing.core.PConstants.RECT;


/**
 * Created by Maashes on 4/17/2016.
 */
public class HUD
{
    PShape _healthBar;
    PFont _stageScore;
    PApplet _sketchParent;
    float _currentPlayerHealth;
    int _currentScore, _maxPlayerHealth;
    String _score;

    public HUD(int currentHealth, int currentStageScore, PApplet parent)
    {
        _maxPlayerHealth = currentHealth;
        _sketchParent = parent;
        _currentScore = currentStageScore;
        _stageScore = _sketchParent.createFont("Arial",16,true);
    }

    public void UpdateHUD(int health, int score)
    {
        _currentScore = score;
        float currentHealth = ((float)health/_maxPlayerHealth)*100;
        _currentPlayerHealth = currentHealth;
        _score = Double.toString(_currentPlayerHealth);
        _healthBar = _sketchParent.createShape(RECT, 0,0, _currentPlayerHealth, 20);
        _healthBar.setFill(_sketchParent.color(163,41,41));
    }

    public void DisplayHUD()
    {
        int xTranslation = -(EngineProvider.GetDefaultEngineInstance().GetXTranslation()-20);
        int yTranslation = -(EngineProvider.GetDefaultEngineInstance().GetYTranslation()-20);
        _sketchParent.shape(_healthBar, xTranslation,yTranslation);
        _sketchParent.textFont(_stageScore,16);
        _sketchParent.text(_score,xTranslation+200,yTranslation);
    }


}
