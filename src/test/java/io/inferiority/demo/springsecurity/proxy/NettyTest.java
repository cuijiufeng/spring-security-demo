package io.inferiority.demo.springsecurity.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author cuijiufeng
 * @Class NettyTest
 * @Date 2023/5/26 16:36
 */
@Slf4j
public class NettyTest {
    @Test
    public void testNetty() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("Netty-Boss", true));
        EventLoopGroup workerGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("Netty-Worker", true));
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 10240)                    //全连接队列的大小
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 内存池
                    .option(ChannelOption.SO_REUSEADDR, true)                   //端口释放后立即就可以被再次使用
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024) // 发送缓冲区大小设置
                    .childOption(ChannelOption.SO_RCVBUF, 1024 * 1024) // 接收缓冲区大小设置
                    .childOption(ChannelOption.SO_KEEPALIVE, false)// 保持长连接状态
                    .childOption(ChannelOption.TCP_NODELAY, true)// 禁用Nagle,使消息立即发出
                    .childOption(ChannelOption.SO_LINGER, 0)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS,30000)
                    .childHandler(new NettyServerChannelInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(5656).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("netty web server startup failed! e:", e);
            System.exit(0);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
