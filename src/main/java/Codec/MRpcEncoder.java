package Codec;

import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FstUtil;
import util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class MRpcEncoder extends MessageToByteEncoder
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx , Object msg , ByteBuf out) throws Exception
    {
        log.info("netty encode");
        byte[] tmp = FstUtil.serialize(msg);
        out.writeInt(tmp.length);
        out.writeBytes(tmp);
    }
}
