package com.shadegame.gameobject.world;

/**
 * Created by Maashes on 4/6/2016.
 */

public class Stage
{
    private int _width, _height, _score;
    private String _backgroundImage;

    public Stage(int width, int height, String bgImage)
    {
        _width = width;
        _height = height;
        _backgroundImage = bgImage;
    }

    public int GetWidth()
    {
        return _width;
    }

    public int GetHeight()
    {
        return _height;
    }

    public String GetBackgroundFile()
    {
        return _backgroundImage;
    }

}
