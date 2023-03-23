package github.kasuminova.kasuminovabot.module.serverhelper.network.handler;

import github.kasuminova.network.message.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHandler<HANDLER> extends SimpleChannelInboundHandler<Object> {
    protected final Map<Class<?>, MessageProcessor<HANDLER, ?>> messageProcessors = new HashMap<>(2);
    protected ChannelHandlerContext ctx;

    @Override
    @SuppressWarnings("unchecked")
    protected final void channelRead0(ChannelHandlerContext ctx, Object msg) {
        MessageProcessor<HANDLER, Object> processor = (MessageProcessor<HANDLER, Object>) messageProcessors.get(msg.getClass());
        if (processor != null) {
            processor.process((HANDLER) this, msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        onRegisterMessages();

        super.channelRegistered(ctx);
    }

    @Override
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;

        channelActive0();

        super.channelActive(ctx);
    }

    @Override
    public final void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelInactive0();

        super.channelInactive(ctx);
    }

    /**
     * 通道启用事件
     */
    protected void channelActive0() {
    }

    /**
     * 通道关闭事件
     */
    protected void channelInactive0() {
    }

    /**
     * 开始注册消息事件
     */
    protected abstract void onRegisterMessages();

    /**
     * 注册消息以及对应的处理器
     * @param clazz 消息类
     * @param processor 消息处理方法
     */
    public <T> void registerMessage(Class<T> clazz, MessageProcessor<HANDLER, T> processor) {
        messageProcessors.put(clazz, processor);
    }
}
