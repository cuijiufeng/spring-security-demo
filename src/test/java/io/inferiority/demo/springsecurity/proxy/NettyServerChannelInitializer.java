package io.inferiority.demo.springsecurity.proxy;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private ChannelInboundHandlerAdapter channelInboundHandler = new NettyServerChannelInboundHandler();
    private ChannelOutboundHandlerAdapter channelOutboundHandler = new NettyServerChannelOutboundHandler();

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler());
        pipeline.addLast("channelInboundHandler", channelInboundHandler);
        //pipeline.addLast("channelOutboundHandler", channelOutboundHandler);
    }
}
