package com.shadegame.engine.utils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Maashes on 6/12/2016.
 */
public class PlatformGroup extends Platform
{
    private ArrayList<Platform> _platformChildren;
    private String _groupName;
    private String[] _images;
    private float _smallestX, _smallestY, _totalWidth,_totalHeight;

    public PlatformGroup(float startX, float starY, float startWidth, float startHeight, String name)
    {
        super(startX,starY,startWidth,startHeight);
        _platformChildren = new ArrayList<>();
        _groupName = name;
    }

    public void AddPlatformChild(Platform p)
    {
        _platformChildren.add(p);

        if(_platformChildren.size() == 1)
        {
            _smallestX = p.XLoc;
            _smallestY = p.YLoc;
            _totalHeight += p.Height;
            _totalWidth = p.Width;
            return;
        }
        SetPlatformType(p.Type);
    }

    public ArrayList<Platform> GetChildren()
    {
        return _platformChildren;
    }

    public String[] GetChildImages()
    {
        return _images;
    }

    public void BuildFullPlatform()
    {
        _platformChildren.sort((platform, t1) -> {
            if(platform.XLoc != t1.XLoc){
                if(platform.XLoc < t1.XLoc)
                {
                    return -1;
                }
                else if(platform.XLoc > t1.XLoc)
                {
                    return 1;
                }
            }
            else if(platform.YLoc != t1.YLoc){
                if(platform.YLoc < t1.YLoc){
                    return -1;
                }
                else if(platform.YLoc > t1.YLoc){
                    return 1;
                }
            }

            return 0;
        });

        if(_platformChildren.size() > 1)
        {
            for(Platform p : _platformChildren)
            {
                if(p.XLoc < _smallestX)
                {
                    _smallestX = p.XLoc;
                }

                if(p.YLoc < _smallestY)
                {
                    _smallestY = p.YLoc;
                }

                if(p.YLoc+p.Height > _smallestY + _totalHeight)
                {
                    _totalHeight += p.Height;
                }

                if(p.XLoc+p.Width > _smallestX + _totalWidth)
                {
                    _totalWidth += p.Width;
                }
            }
        }


        XLoc = _smallestX;
        YLoc = _smallestY;
        Width = _totalWidth;
        Height = _totalHeight;
        _images = new String[_platformChildren.size()];


        int imageIdx = 0;
        for(Platform p : _platformChildren)
        {
            _images[imageIdx] = p.Image;
            imageIdx++;
        }
    }

}