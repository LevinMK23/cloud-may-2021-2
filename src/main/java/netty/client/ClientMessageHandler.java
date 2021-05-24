package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.Message;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final Callback callback;

    public ClientMessageHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        callback.call(message);
    }
}
