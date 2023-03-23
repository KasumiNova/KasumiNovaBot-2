package github.kasuminova.kasuminovabot.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.yamlkt.Comment

object BotData : AutoSavePluginConfig("BotData") {
    data class Data(
        @Comment("当前机器人的黑名单 QQ 列表，处于 QQ 黑名单内的成员不能执行指令，且会被自动拒绝加群。")
        var commandBlackList: List<Long> = mutableListOf(),
    )

    private val botDataList : Map<Long, Data> = mutableMapOf()

    fun getBotData(botId : Long): Data {
        return if (botDataList.containsKey(botId)) {
            botDataList[botId]!!
        } else {
            val data = Data()
            botDataList[botId] to data
            data;
        }
    }
}