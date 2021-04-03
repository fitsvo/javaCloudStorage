package main;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

import static java.nio.file.Files.*;


public class FileSendOptimizer
{

    private static final int SLICE_SIZE = 4 * 1024 * 1024;


    public static void sendFile(Path path, Consumer<DataPackage> sendAction)
            throws Exception
    {
        if (!exists(path)) return;

        if (size(path) < SLICE_SIZE * 8)
            sendFull(path, sendAction);
        else
            sendByChunks(path, sendAction);
    }


    private static void sendFull(Path path, Consumer<DataPackage> sendAction)
            throws Exception
    {
        DataPackage pack = new FileDataPackage(path);
        sendAction.accept(pack);
    }


    private static void sendByChunks(Path path, Consumer<DataPackage> sendAction)
            throws Exception
    {
        try (InputStream in = newInputStream(path))
        {
            int availCount = in.available();
            int rem = availCount % SLICE_SIZE;

            byte[] chunk = new byte[SLICE_SIZE];
            byte[] chunkLast = rem != 0 ? new byte[rem] : new byte[SLICE_SIZE];

            int num = 1;
            while (availCount > SLICE_SIZE)
            {
                in.read(chunk);
                DataPackage pack = new FileSlicePackage(path, chunk, num++);
                sendAction.accept(pack);
                availCount = in.available();
            }

            in.read(chunkLast);
            DataPackage pack = new FileSlicePackage(path, chunkLast);
            sendAction.accept(pack);
        }
    }

}
