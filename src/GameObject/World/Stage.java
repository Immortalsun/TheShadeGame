package GameObject.World;

/**
 * Created by Maashes on 4/6/2016.
 */

import GameObject.GameObject;
import processing.core.PVector;

public class Stage
{
    private int _width, _height, _score;
    private String _backgroundImage;

    public Stage(int width, int height)
    {
        _width = width;
        _height = height;
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
