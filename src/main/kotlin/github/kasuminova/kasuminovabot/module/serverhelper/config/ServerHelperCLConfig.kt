package github.kasuminova.kasuminovabot.module.serverhelper.config

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment

@ValueDescription("新星工程：辅助处理器配置")
object ServerHelperCLConfig : AutoSavePluginConfig("ServerHelperClient") {

    @Serializable
    data class CLConfig(
        @Comment("将游戏消息发送到群聊的机器人 QQ。")
        var botId: Long = 1234567890L,

        @Comment("接收信息与同步游戏消息的群聊。")
        var groupID: Long = 9876543210L,

        @Comment("BC 插件服务端的 IP 地址，理论支持 IPv6。")
        var ip: String = "127.0.0.1",

        @Comment("BC 插件服务端的端口。")
        var port: Int = 20000,

        @Comment("AccessToken，必须与中心服务器的 ManagerAccessToken 相同才可使用。")
        var accessToken: String = "abc123",

        @Comment("群服聊天互通的开始时间，00:00:00 为全天。")
        var chatSyncStartTime: String = "00:00:00",

        @Comment("群服聊天互通的结束时间，23:59:59 为全天。")
        var chatSyncEndTime: String = "23:59:59",

        @Comment("新星工程：核心模组的 HyperNet 体系研究描述文件名。")
        var researchDataFileName: String = "hypernet_research_data.json",
    )

    val clients: MutableList<CLConfig> by value(
        mutableListOf(
            CLConfig(
                botId = 1234567890L,
                groupID = 9876543210L,
                ip = "127.0.0.1",
                port = 20000,
                accessToken = "abc123",
                chatSyncStartTime = "00:00:00",
                chatSyncEndTime = "00:00:00",
                researchDataFileName = "hypernet_research_data.json"
            )
        )
    )
}
