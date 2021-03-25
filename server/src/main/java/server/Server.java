package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import static io.netty.handler.codec.serialization.ClassResolvers.cacheDisabled;

public class Server {
    private static ServerBootstrap sb;
    private final EventLoopGroup mainGroup;
    private final EventLoopGroup workerGroup;

    static final int PORT = 8888;
    static final String STORAGE_DIR = "server/server_storage";

    public Server() {
        ServerBootstrap sb = new ServerBootstrap();
        mainGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        sb.group(mainGroup, workerGroup);
        sb.channel(NioServerSocketChannel.class);
        sb.childHandler(new SocketChannelInitializer());
        sb.childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public static void start() throws InterruptedException {
            ChannelFuture future = sb.bind(PORT).sync();
            future.channel().closeFuture();
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        try{
            start();
        }
        finally{
            server.mainGroup.shutdownGracefully();
            server.workerGroup.shutdownGracefully();
        }
    }


    private static class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {
        private static final int MAXSIZE = 100*1024*1024;
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelHandler encoding = new ObjectEncoder();
            ChannelHandler decoding = new ObjectDecoder(MAXSIZE, cacheDisabled(null));
            ChannelHandler auth = (ChannelHandler) new AuthService();
            ChannelHandler main = new MainHandler();

            socketChannel.pipeline().addLast(decoding, encoding, auth, main);

        }
    }
}
