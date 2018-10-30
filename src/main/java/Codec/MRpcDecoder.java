package Codec;

import util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.logging.Logger;

public class MRpcDecoder extends ByteToMessageDecoder
{
    private Logger log = Logger.getLogger(this.getClass().getName());

    private Class clazz;

    public MRpcDecoder(Class c){
        this.clazz = c;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx , ByteBuf in , List<Object> out) throws Exception
    {
        log.info("netty server decode");
        int length = in.readInt();
        byte[] tmp = new byte[length];
        in.readBytes(tmp);
        out.add(SerializationUtil.deserialize(tmp, clazz));
    }
}
