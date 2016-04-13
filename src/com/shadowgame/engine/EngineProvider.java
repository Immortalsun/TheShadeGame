package com.shadowgame.engine;

/**
 * Created by Maashes on 4/10/2016.
 */
public class EngineProvider
{
    static Engine _engine;

    public static Engine GetDefaultEngineInstance()
    {
        return _engine;
    }

    public static void SetDefaultEngineInstance(Engine e)
    {
        _engine = e;
    }
}
