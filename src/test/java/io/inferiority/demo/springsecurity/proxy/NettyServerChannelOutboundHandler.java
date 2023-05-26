package io.inferiority.demo.springsecurity.proxy;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

@ChannelHandler.Sharable
public class NettyServerChannelOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
        ctx.close();
    }
}
