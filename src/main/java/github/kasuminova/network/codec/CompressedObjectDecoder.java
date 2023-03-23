package github.kasuminova.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import org.xerial.snappy.SnappyInputStream;

public class CompressedObjectDecoder extends LengthFieldBasedFrameDecoder {
    private final ClassLoader classLoader;

    public CompressedObjectDecoder(ClassLoader classLoader) {
        super(1048576, 0, 4, 0, 4);
        this.classLoader = classLoader;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        ByteBufInputStream bis = new ByteBufInputStream(frame);
        SnappyInputStream snappy = new SnappyInputStream(bis);
        ObjectDecoderInputStream ois = new ObjectDecoderInputStream(snappy, classLoader);

        try {
            return ois.readObject();
        } finally {
            ois.close();
            snappy.close();
            bis.close();
        }
    }
}
