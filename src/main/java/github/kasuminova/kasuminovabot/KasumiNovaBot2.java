package github.kasuminova.kasuminovabot;

import github.kasuminova.kasuminovabot.command.GroupCommand;
import github.kasuminova.kasuminovabot.command.HelpCmd;
import github.kasuminova.kasuminovabot.data.BotData;
import github.kasuminova.kasuminovabot.handler.globallistener.GlobalGroupMessageListener;
import github.kasuminova.kasuminovabot.handler.sublistener.CommandListener;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.ConstPool;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class KasumiNovaBot2 extends JavaPlugin {
    public static final KasumiNovaBot2 INSTANCE = new KasumiNovaBot2();
    public static final List<Long> SUPER_ADMIN = Arrays.asList(3044344887L, 2755271615L);
    public static final String PROTOCOL_VERSION = "1.0.0";
    public static final char COMMAND_PREFIX = '#';
    public final MiraiLogger logger = getLogger();
    public final GlobalGroupMessageListener globalGroupMessageListener = new GlobalGroupMessageListener();
    private final HashMap<String, GroupCommand> registeredCommands = new HashMap<>();
    private final HashMap<String, HashMap<String, GroupCommand>> privateGroupCommands = new HashMap<>();
    public final CommandListener commandListener = new CommandListener(registeredCommands, privateGroupCommands, globalGroupMessageListener);

    private KasumiNovaBot2() {
        super(new JvmPluginDescriptionBuilder("github.kasuminova.kasuminovabot.KasumiNovaBot2", "2.2.0")
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

        logger.info("载入监听器...");
        registerCommand(HelpCmd.INSTANCE.commandName, HelpCmd.INSTANCE);
        commandListener.load();
        globalGroupMessageListener.subscribe();

        logger.info("载入服务器助手功能...");
        ServerHelperCL.loadConsoleCommand();
        ServerHelperCL.loadAll();
    }

    @Override
    public void onDisable() {
        logger.info("卸载监听器...");
        unregisterCommand(HelpCmd.INSTANCE.commandName);
        commandListener.unLoad();
        globalGroupMessageListener.unSubscribe();

        logger.info("卸载服务器助手功能...");
        ServerHelperCL.unloadAll();
    }

    public void registerCommand(String commandStr, GroupCommand command) {
        registeredCommands.put(commandStr, command);
    }

    public void unregisterCommand(String commandStr) {
        registeredCommands.remove(commandStr);
    }

    public void registerPrivateCommand(String groupId, HashMap<String, GroupCommand> commands) {
        privateGroupCommands.put(groupId, commands);
    }

    public void unregisterPrivateCommand(String groupId) {
        privateGroupCommands.remove(groupId);
    }

    public HashMap<String, GroupCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    public HashMap<String, HashMap<String, GroupCommand>> getPrivateGroupCommands() {
        return privateGroupCommands;
    }

}
