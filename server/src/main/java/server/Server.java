package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import static io.netty.channel.ChannelOption.SO_KEEPALIVE;
import static io.netty.handler.codec.serialization.ClassResolvers.cacheDisabled;


public class Server
{

    private ServerBootstrap sb;
    private EventLoopGroup mainGroup;
    private EventLoopGroup workerGroup;

    static final int PORT = 8888;
    static final String STORAGE_DIR = "server/server_storage";


    public Server()
    {
        sb = new ServerBootstrap();
        mainGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        sb.group(mainGroup, workerGroup);
        sb.channel(NioServerSocketChannel.class);
        sb.childHandler(new SocketChannelInitializer());
        sb.childOption(SO_KEEPALIVE, true);
    }


    private void start()
            throws InterruptedException
    {
        ChannelFuture future = sb.bind(PORT).sync();
        future.channel().closeFuture().sync();
    }


    public static void main(String[] args)
            throws Exception
    {
        Server server = new Server();
        try
        {
            server.start();
        }
        finally
        {
            server.mainGroup.shutdownGracefully();
            server.workerGroup.shutdownGracefully();
        }
    }


    private static class SocketChannelInitializer
            extends ChannelInitializer<SocketChannel>
    {

        private static final int MAX_OBJ_SIZE =   1024 * 1024;


        @Override
        protected void initChannel(SocketChannel ch)
        {
            ChannelHandler decoder = new ObjectDecoder(MAX_OBJ_SIZE, cacheDisabled(null));
            ChannelHandler encoder = new ObjectEncoder();
            ChannelHandler auth = new AuthHandler();
            ChannelHandler main = new MainHandler();

            ch.pipeline().addLast(decoder, encoder, auth, main);
        }

    }

}
