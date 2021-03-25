package main;

import java.io.Serializable;


public abstract class DataPackage implements Serializable
{

    @Override
    public String toString()
    {
        return getClass().getSimpleName();
    }

}
