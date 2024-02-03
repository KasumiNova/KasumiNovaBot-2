package github.kasuminova.kasuminovabot;

import github.kasuminova.kasuminovabot.command.*;
import github.kasuminova.kasuminovabot.data.BotData;
import github.kasuminova.kasuminovabot.event.listener.GroupMessageListener;
import github.kasuminova.kasuminovabot.event.processor.GroupCommandProcessor;
import github.kasuminova.kasuminovabot.module.entertainment.TipManagerSyncThread;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.ConstPool;
import github.kasuminova.kasuminovabot.util.FileUtil;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public final class KasumiNovaBot2 extends JavaPlugin {
    public static final KasumiNovaBot2 INSTANCE = new KasumiNovaBot2();

    public static final List<Long> SUPER_ADMIN = Arrays.asList(3044344887L, 2755271615L);
    public static final String PROTOCOL_VERSION = "1.0.0";
    public static final char COMMAND_PREFIX = '#';

    public final MiraiLogger logger = getLogger();

    private final Map<String, GroupCommand> registeredCommands = new LinkedHashMap<>();
    private final Map<String, Map<String, GroupCommand>> privateGroupCommands = new LinkedHashMap<>();

    public final GroupMessageListener genericEventListener = new GroupMessageListener();
    public final GroupCommandProcessor commandListener = new GroupCommandProcessor(registeredCommands, privateGroupCommands, genericEventListener);

    private TipManagerSyncThread tipManagerSyncThread = null;

    private KasumiNovaBot2() {
        super(new JvmPluginDescriptionBuilder("github.kasuminova.kasuminovabot.KasumiNovaBot2", "2.4.0")
                .name("KasumiNovaBot2")
                .author("KasumiNova")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        logger.info(ConstPool.PLUGIN_TITLE);
        logger.info("KasumiNovaBot 2 开始载入！");
    }

    @Override
    public void onEnable() {
        logger.info("载入配置文件...");
        reloadPluginConfig(BotData.INSTANCE);
        loadRandomTips();

        logger.info("载入监听器...");
        registerCommand(HelpCmd.INSTANCE.commandName, HelpCmd.INSTANCE);
        registerCommand(RandomHitokotoCmd.INSTANCE.commandName, RandomHitokotoCmd.INSTANCE);
        registerCommand(ChunkPosCalculatorCmd.INSTANCE.commandName, ChunkPosCalculatorCmd.INSTANCE);
        registerCommand(CPUAffinityMaskCalculatorCmd.INSTANCE.commandName, CPUAffinityMaskCalculatorCmd.INSTANCE);
        registerCommand(SubmitTipCmd.INSTANCE.commandName, SubmitTipCmd.INSTANCE);
        registerCommand(RemoveTipCmd.INSTANCE.commandName, RemoveTipCmd.INSTANCE);
        registerCommand(ApproveTipCmd.INSTANCE.commandName, ApproveTipCmd.INSTANCE);
        registerCommand(TipStatusCmd.INSTANCE.commandName, TipStatusCmd.INSTANCE);

        commandListener.load();
        genericEventListener.subscribe();

        logger.info("载入服务器助手功能...");
        ServerHelperCL.loadConsoleCommand();
        ServerHelperCL.loadAll();
    }

    @Override
    public void onDisable() {
        unloadRandomTips();

        logger.info("卸载监听器...");
        unregisterCommand(HelpCmd.INSTANCE.commandName);
        unregisterCommand(RandomHitokotoCmd.INSTANCE.commandName);
        unregisterCommand(ChunkPosCalculatorCmd.INSTANCE.commandName);
        unregisterCommand(CPUAffinityMaskCalculatorCmd.INSTANCE.commandName);
        unregisterCommand(SubmitTipCmd.INSTANCE.commandName);
        unregisterCommand(RemoveTipCmd.INSTANCE.commandName);
        unregisterCommand(ApproveTipCmd.INSTANCE.commandName);
        unregisterCommand(TipStatusCmd.INSTANCE.commandName);

        commandListener.unLoad();
        genericEventListener.unSubscribe();

        logger.info("卸载服务器助手功能...");
        ServerHelperCL.unloadAll();
    }

    private void loadRandomTips() {
        File file = KasumiNovaBot2.INSTANCE.resolveDataFile("random_tips.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.warning("随机提示文件创建失败！", e);
            }
        } else {
            try {
                TipManager.decodeJsonData(FileUtil.readStringFromFile(file));
                logger.info("随机提示文件载入成功！");
            } catch (IOException e) {
                logger.warning("随机提示文件读取失败！", e);
            }
        }
        if (file.exists()) {
            try {
                tipManagerSyncThread = new TipManagerSyncThread(new RandomAccessFile(file, "rws"));
                tipManagerSyncThread.start();
            } catch (FileNotFoundException e) {
                logger.warning("随机提示文件创建失败！", e);
            }
        }
    }

    private void unloadRandomTips() {
        if (tipManagerSyncThread != null) {
            tipManagerSyncThread.interrupt();
        }
    }

    public void registerCommand(String commandStr, GroupCommand command) {
        registeredCommands.put(commandStr, command);
    }

    public void unregisterCommand(String commandStr) {
        registeredCommands.remove(commandStr);
    }

    public void registerPrivateCommand(String groupId, Map<String, GroupCommand> commands) {
        privateGroupCommands.put(groupId, commands);
    }

    public void unregisterPrivateCommand(String groupId) {
        privateGroupCommands.remove(groupId);
    }

    public Map<String, GroupCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    public Map<String, Map<String, GroupCommand>> getPrivateGroupCommands() {
        return privateGroupCommands;
    }

}
