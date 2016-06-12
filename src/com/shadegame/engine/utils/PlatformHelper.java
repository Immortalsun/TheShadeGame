package com.shadegame.engine.utils;

/**
 * Created by Maashes on 6/12/2016.
 */
public class PlatformHelper
{

    public static PlatformType GetPlatformType(String platformImage)
    {
        if(platformImage.contains("basic"))
        {
            return PlatformType.BASIC;
        }

        return PlatformType.BASIC;
    }

    public static float[] GetPlatformBoundingBoxOffsets(PlatformType type)
    {
        float[] bounds = new float[4];

        switch(type)
        {
            case BASIC:
                bounds[0] = 0;
                bounds[1] = 2;
                bounds[2] = 0;
                bounds[3] = -4;
                break;
        }

        return bounds;
    }


}


