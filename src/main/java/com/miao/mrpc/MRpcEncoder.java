package com.miao.mrpc;

import com.miao.mrpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.logging.Logger;

public class MRpcEncoder extends MessageToByteEncoder<RpcRequest>
{
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    protected void encode(ChannelHandlerContext ctx , RpcRequest msg , ByteBuf out) throws Exception
    {
        log.info("netty client encode");
        byte[] tmp = SerializationUtil.serialize(msg);
        out.writeInt(tmp.length);
        out.writeBytes(tmp);
    }
}
