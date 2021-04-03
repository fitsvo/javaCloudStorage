package client;

import main.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import static client.Controller.STORAGE_DIR;
import static java.lang.Thread.currentThread;

public class ServerResponseHandler implements Runnable
{

    private final NetConnection conn;
    private Consumer<List<String>> callbackFileList;
    private Runnable callbackFileData;
    private FileSliceSaver saver;


    public ServerResponseHandler()
    {
        conn = Main.getNetConnection();
        saver = new FileSliceSaver(Paths.get(STORAGE_DIR));
    }


    public void setFileListActionUI(Consumer<List<String>> action)
    {
        callbackFileList = action;
    }


    public void setFileDataActionUI(Runnable action)
    {
        callbackFileData = action;
    }


    @Override
    public void run()
    {
        try
        {
            while (!currentThread().isInterrupted())
            {
                DataPackage response = conn.getResponseFromServer();
                processResponse(response);
            }
        }
        catch (NetConnection.ServerResponseException | IOException e)
        {
            e.printStackTrace();
        }
    }


    private void processResponse(DataPackage response) throws IOException
    {
        if (response instanceof FileListCommand)
        {
            FileListCommand com = (FileListCommand) response;
            callbackFileList.accept(com.getFileNames());
            return;
        }

        if (response instanceof FileDataPackage)
        {
            FileDataPackage pack = (FileDataPackage) response;
            Path path = Paths.get(STORAGE_DIR + "/" + pack.getFilename());
            Files.write(path, pack.getData());
            callbackFileData.run();
            return;
        }

        if (response instanceof FileSlicePackage)
        {
            saver.writeFileSlice((FileSlicePackage) response, () -> callbackFileData.run());
        }
    }

}
