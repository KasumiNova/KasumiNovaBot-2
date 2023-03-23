package github.kasuminova.kasuminovabot.module.serverhelper.network;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.module.serverhelper.network.handler.MainHandler;
import github.kasuminova.network.codec.CompressedObjectDecoder;
import github.kasuminova.network.codec.CompressedObjectEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final ServerHelperCL cl;
    public ClientInitializer(ServerHelperCL cl) {
        this.cl = cl;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

//        pipeline.addLast(new ObjectEncoder());
//        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingResolver(KasumiNovaBot2.class.getClassLoader())));
        pipeline.addLast("CompressedObjectEncoder", new CompressedObjectEncoder());
        pipeline.addLast("CompressedObjectDecoder", new CompressedObjectDecoder(KasumiNovaBot2.class.getClassLoader()));

        pipeline.addLast("MainHandler", new MainHandler(cl));
    }
}
