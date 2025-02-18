package github.kasuminova.kasuminovabot.module.serverhelper.network.handler;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.data.BotData;
import github.kasuminova.kasuminovabot.module.serverhelper.GroupMessages;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerMessageSyncThread;
import github.kasuminova.kasuminovabot.module.serverhelper.config.ServerHelperCLConfig;
import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.ChatFilter;
import github.kasuminova.kasuminovabot.util.FilteredStr;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.chatmessage.GameChatMessage;
import github.kasuminova.network.message.playercmd.PlayerCmdExecFailedMessage;
import github.kasuminova.network.message.protocol.ClientType;
import github.kasuminova.network.message.protocol.ClientTypeMessage;
import github.kasuminova.network.message.protocol.HeartbeatResponse;
import github.kasuminova.network.message.protocol.PreDisconnectMessage;
import github.kasuminova.network.message.servercmd.CmdExecFailedMessage;
import github.kasuminova.network.message.servercmd.CmdExecResultsMessage;
import github.kasuminova.network.message.serverinfo.OnlinePlayerListMessage;
import github.kasuminova.network.message.whitelist.*;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainHandler extends AbstractHandler<MainHandler> implements UpdateType, ResultCode {
    final ServerHelperCL cl;
    final ServerHelperCLConfig.CLConfig config;

    public MainHandler(ServerHelperCL cl) {
        this.cl = cl;
        this.config = cl.getConfig();
    }

    private static void sendGameChatMessage(MainHandler handler, GameChatMessage message) {
        ServerMessageSyncThread syncTask = handler.cl.getChatMessageSyncTask();
        if (!syncTask.canSendChatMessage()) {
            return;
        }
        BotData.Data botData = BotData.INSTANCE.getBotData(handler.cl.getConfig().getBotId());
        FilteredStr filtered = ChatFilter.filterChat(message.message, botData.getInappropriateWordsCN(), botData.getInappropriateWordsEN());
        if (filtered.isFiltered()) {
            KasumiNovaBot2.INSTANCE.logger.warning("接收到敏感词匹配的聊天信息：" + message.userName + "：" + message.message);
            KasumiNovaBot2.INSTANCE.logger.warning("匹配词汇：" + filtered.filteredString());
        } else {
            KasumiNovaBot2.INSTANCE.logger.info("接收到聊天信息：" + message.userName + "：" + message.message);
            syncTask.messageQueue.offer(message);
        }
    }

    private static void sendPlayerListMessage(MainHandler handler, OnlinePlayerListMessage message) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前服务器在线人数：").append(String.valueOf(message.totalOnline)).append("，且服务器正常运行。");

        String[] playerList = message.playerList;
        if (message.totalOnline != 0 && playerList.length != 0) {
            builder.append(MiraiCodes.WRAP).append("在线玩家：").append(MiraiCodes.WRAP);

            int counter = 1;
            for (int i = 0; i < Math.min(playerList.length, 40); i++) {
                if (i == playerList.length - 1) {
                    builder.append(playerList[i]);
                } else if (counter <= 3) {
                    builder.append(playerList[i]).append("、");
                    counter++;
                } else {
                    builder.append(playerList[i]).append(MiraiCodes.WRAP);
                    counter = 1;
                }
            }
            if (playerList.length > 40) {
                builder.append(MiraiCodes.WRAP)
                        .append("还有 ").append(String.valueOf(playerList.length - 40)).append(" 位玩家未展示...");
            }
        }

        ThreadLocalRandom rd = ThreadLocalRandom.current();
        if (rd.nextFloat() >= 0.75F) {
            CustomTip customTip = TipManager.randomTip();
            if (customTip != null) {
                builder.append(MiraiCodes.WRAP).append(customTip.getTip()).append(" —— ").append(customTip.getWho())
                        .append("(ID: ").append(String.valueOf(customTip.getId())).append(")");
                if (rd.nextFloat() >= 0.25F) {
                    switch (rd.nextInt(3)) {
                        case 0: {
                            builder.append(MiraiCodes.WRAP).append("想要留下自己的随机提示，请使用《#投稿随机提示》~！");
                            break;
                        }
                        case 1: {
                            builder.append(MiraiCodes.WRAP).append("你说得对，但是《#投稿随机提示》可以在这里留下你的提示！");
                            break;
                        }
                        case 2: {
                            builder.append(MiraiCodes.WRAP).append("《#投稿随机提示》是好东西。");
                            break;
                        }
                    }
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
                builder.append(GroupMessages.WHITELIST_ID_NOT_EXIST);
                break;
            case ID_NOT_EXIST:
                builder.append(GroupMessages.WHITELIST_QQ_NOT_EXIST);
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

    private static void offerExecResult(MainHandler handler, PlayerCmdExecFailedMessage message) {
        handler.cl.getChatMessageSyncTask().offerCmdExecResult(message.playerName, Collections.singletonList(message.cause));
    }

    private static void checkUserInGroup(MainHandler handler, UserInGroupQueryMessage message) {
        Bot bot = Bot.getInstance(handler.cl.getConfig().getBotId());
        try {
            Group group = bot.getGroup(handler.cl.getConfig().getGroupID());
            if (group == null) {
                handler.ctx.writeAndFlush(new UserInGroupResultMessage(message.id, false));
                return;
            }

            NormalMember member = group.get(message.id);
            if (member == null) {
                handler.ctx.writeAndFlush(new UserInGroupResultMessage(message.id, false));
                return;
            }

            handler.ctx.writeAndFlush(new UserInGroupResultMessage(message.id, true));
        } catch (Throwable e) {
            handler.ctx.writeAndFlush(new UserInGroupResultMessage(message.id, false));
        }
    }

    private static void preDisconnect(MainHandler handler, PreDisconnectMessage message) {
        KasumiNovaBot2.INSTANCE.logger.warning("即将从中心服务器断开连接，原因：" + message.reason);
//        handler.cl.disconnect();
    }

    @Override
    protected void onRegisterMessages() {
        registerMessage(WhiteListUpdateResult.class, MainHandler::sendWhiteListUpdateResult);

        registerMessage(GameChatMessage.class, MainHandler::sendGameChatMessage);
        registerMessage(OnlinePlayerListMessage.class, MainHandler::sendPlayerListMessage);

        registerMessage(CmdExecFailedMessage.class, MainHandler::offerExecResult);
        registerMessage(CmdExecResultsMessage.class, MainHandler::offerExecResult);

        registerMessage(PlayerCmdExecFailedMessage.class, MainHandler::offerExecResult);
        registerMessage(UserInGroupQueryMessage.class, MainHandler::checkUserInGroup);

        registerMessage(PreDisconnectMessage.class, MainHandler::preDisconnect);
        registerMessage(HeartbeatResponse.class, (handler, message) -> cl.updateHeartbeatTime());
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
