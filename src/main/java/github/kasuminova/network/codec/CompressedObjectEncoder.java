package github.kasuminova.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.Serializable;

public class CompressedObjectEncoder extends MessageToByteEncoder<Serializable> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();

        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        SnappyOutputStream snappy = new SnappyOutputStream(bout);
        ObjectEncoderOutputStream oos = new ObjectEncoderOutputStream(snappy);

        try {
            bout.write(LENGTH_PLACEHOLDER);
            oos.writeObject(msg);
        } finally {
            oos.close();
            snappy.close();
            bout.close();
        }

        int endIdx = out.writerIndex();

        out.setInt(startIdx, endIdx - startIdx - 4);
    }
}
