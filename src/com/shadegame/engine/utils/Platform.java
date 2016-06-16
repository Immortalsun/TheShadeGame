package com.shadegame.engine.utils;

/**
 * Created by Maashes on 6/12/2016.
 */
public class Platform
{
    public float XLoc;
    public float YLoc;
    public float Width;
    public float Height;
    public String Image;
    public PlatformType Type;

    public Platform(float x, float y, float width, float height)
    {
        XLoc = x;
        YLoc = y;
        Width = width;
        Height = height;
    }

    public void SetPlatformImage(String img)
    {
        Image = img;
    }

    public void SetPlatformType(PlatformType type)
    {
        Type = type;
    }
}

