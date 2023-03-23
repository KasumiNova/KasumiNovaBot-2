package github.kasuminova.kasuminovabot.module.serverhelper.network.handler;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.GroupMessages;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerMessageSyncThread;
import github.kasuminova.kasuminovabot.module.serverhelper.config.ServerHelperCLConfig;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.chatmessage.GameChatMessage;
import github.kasuminova.network.message.protocol.ClientType;
import github.kasuminova.network.message.protocol.ClientTypeMessage;
import github.kasuminova.network.message.protocol.PreDisconnectMessage;
import github.kasuminova.network.message.servercmd.CmdExecFailedMessage;
import github.kasuminova.network.message.servercmd.CmdExecResultsMessage;
import github.kasuminova.network.message.serverinfo.OnlinePlayerListMessage;
import github.kasuminova.network.message.whitelist.FullWhiteListInfo;
import github.kasuminova.network.message.whitelist.ResultCode;
import github.kasuminova.network.message.whitelist.UpdateType;
import github.kasuminova.network.message.whitelist.WhiteListUpdateResult;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainHandler extends AbstractHandler<MainHandler> implements UpdateType, ResultCode {
    final ServerHelperCL cl;
    final ServerHelperCLConfig.CLConfig config;

    public MainHandler(ServerHelperCL cl) {
        this.cl = cl;
        this.config = cl.getConfig();
    }

    private static void sendGameChatMessage(MainHandler handler, GameChatMessage message) {
        ServerMessageSyncThread syncTask = handler.cl.getChatMessageSyncTask();
        if (syncTask.canSendChatMessage()) {
            syncTask.messageQueue.offer(message);
        }
    }

    private static void sendPlayerListMessage(MainHandler handler, OnlinePlayerListMessage message) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前服务器在线人数：").append(String.valueOf(message.totalOnline)).append("，且服务器正常运行。");

        String[] playerList = message.playerList;
        if (message.totalOnline != 0 && playerList.length != 0) {
            builder.append(MiraiCodes.WRAP).append("在线玩家：").append(MiraiCodes.WRAP);

            int count = 1;
            for (int i = 0; i < playerList.length; i++) {
                if (i == playerList.length - 1) {
                    builder.append(playerList[i]);
                } else if (count <= 3) {
                    builder.append(playerList[i]).append("、");
                    count++;
                } else {
                    builder.append(playerList[i]).append(MiraiCodes.WRAP);
                    count = 1;
                }
            }
        }

        handler.cl.sendMessage(builder.build());
    }

    private static void sendWhiteListUpdateResult(MainHandler handler, WhiteListUpdateResult message) {
        MessageChainBuilder builder = new MessageChainBuilder();
        FullWhiteListInfo fullWhiteListInfo = message.getFullWhiteListInfo();
        boolean unExpected = false;
        switch (message.getResultCode()) {
            case SUCCESS: {
                switch (message.getUpdateType()) {
                    case ADD:
                        builder.append(GroupMessages.whiteListAddSucceeded(fullWhiteListInfo));
                        break;
                    case GET:
                        builder.append(GroupMessages.whiteListInfo(fullWhiteListInfo));
                        break;
                    case UPDATE:
                        builder.append(GroupMessages.whiteListUpdateSucceeded(fullWhiteListInfo));
                        break;
                    case REMOVE:
                        builder.append(GroupMessages.whiteListRemoveSucceeded(fullWhiteListInfo));
                        break;
                    default:
                        unExpected = true;
                }
                break;
            }
            case USERNAME_ALREADY_EXISTS:
                builder.append(GroupMessages.whiteListUserNameAlreadyExists(fullWhiteListInfo));
                break;
            case ID_ALREADY_EXISTS:
                builder.append(GroupMessages.whiteListIDAlreadyExists(fullWhiteListInfo));
                break;
            case USERNAME_NOT_EXIST:
                builder.append(GroupMessages.WHITELIST_USERNAME_NOT_EXIST);
                break;
            case ID_NOT_EXIST:
                builder.append(GroupMessages.WHITELIST_ID_NOT_EXIST);
                break;
            default:
                unExpected = true;
        }

        if (unExpected) {
            handler.cl.sendMessage(builder.append("发现预料之外的内部错误，请联系管理员。").build());
            KasumiNovaBot2.INSTANCE.logger.warning(String.format(
                    "Unexpected WhiteListUpdateResult: resultCode %s, updateType %s",
                    message.getResultCode(), message.getUpdateType()));
        } else {
            handler.cl.sendMessage(builder.build());
        }
    }

    private static void offerExecResult(MainHandler handler, CmdExecFailedMessage message) {
        handler.cl.getChatMessageSyncTask().offerCmdExecResult(message.serverName, Collections.singletonList(message.cause));
    }

    private static void offerExecResult(MainHandler handler, CmdExecResultsMessage message) {
        List<String> plainText = new ArrayList<>();
        for (String result : message.results) {
            plainText.add(result.replaceAll("§.", ""));
        }
        handler.cl.getChatMessageSyncTask().offerCmdExecResult(message.serverName, plainText);
    }

    private static void preDisconnect(MainHandler handler, PreDisconnectMessage message) {
        KasumiNovaBot2.INSTANCE.logger.warning("即将从中心服务器断开连接，原因：" + message.reason);
        handler.cl.disconnect();
    }

    @Override
    protected void onRegisterMessages() {
        registerMessage(WhiteListUpdateResult.class, MainHandler::sendWhiteListUpdateResult);

        registerMessage(GameChatMessage.class, MainHandler::sendGameChatMessage);
        registerMessage(OnlinePlayerListMessage.class, MainHandler::sendPlayerListMessage);

        registerMessage(CmdExecFailedMessage.class, MainHandler::offerExecResult);
        registerMessage(CmdExecResultsMessage.class, MainHandler::offerExecResult);

        registerMessage(PreDisconnectMessage.class, MainHandler::preDisconnect);
    }

    @Override
    protected void channelActive0() {
        KasumiNovaBot2.INSTANCE.logger.info("成功连接至中心服务器，认证中...");
        ctx.writeAndFlush(new ClientTypeMessage(
                ClientType.MANAGER.toString(),
                KasumiNovaBot2.PROTOCOL_VERSION,
                config.getAccessToken(),
                String.valueOf(config.getBotId())));

        cl.setCtx(ctx);
    }

    @Override
    protected void channelInactive0() {
        KasumiNovaBot2.INSTANCE.logger.info("从插件服务器断开连接.");
        cl.future = null;
        cl.setCtx(null);
    }
}
