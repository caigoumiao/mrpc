package Codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author larry miao
 * @date 2018-11-11
 */
public class MRpcDecoder extends ByteToMessageDecoder
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx , ByteBuf in , List<Object> out)
    {
        log.info("netty decode");
        int length = in.readInt();
        byte[] tmp = new byte[length];
        in.readBytes(tmp);
        out.add(FstUtil.deserialize(tmp));
    }
}
