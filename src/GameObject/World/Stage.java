package GameObject.World;

/**
 * Created by Maashes on 4/6/2016.
 */

import GameObject.GameObject;
import processing.core.PVector;

public class Stage
{
    private int _width, _height, _score;

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

}
