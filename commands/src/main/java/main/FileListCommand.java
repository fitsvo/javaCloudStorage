package main;

import java.util.List;

public class FileListCommand extends DataPackage
{

    private List<String> filenames;


    public void setFileNames(List<String> filenames)
    {
        this.filenames = filenames;
    }


    public List<String> getFileNames()
    {
        return filenames;
    }

}
