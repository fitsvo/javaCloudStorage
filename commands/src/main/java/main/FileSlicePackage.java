package main;

import java.nio.file.Path;

public class FileSlicePackage extends DataPackage
{

    private final String filename;
    private final byte[] data;
    private final int num;
    private final boolean last;


    public FileSlicePackage(Path path, byte[] slice, int num)
    {
        filename = path.getFileName().toString();
        data = slice;
        this.num = num;
        last = false;
    }


    public FileSlicePackage(Path path, byte[] slice)
    {
        filename = path.getFileName().toString();
        data = slice;
        this.num = -1;
        last = true;
    }


    public String getFilename()
    {
        return filename;
    }


    public int getNum()
    {
        return num;
    }


    public byte[] getData()
    {
        return data;
    }


    public boolean isLast()
    {
        return last;
    }


    public boolean isFirst()
    {
        return num == 1;
    }

}
