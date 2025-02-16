package github.kasuminova.kasuminovabot.data

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment

object BotData : AutoSavePluginConfig("BotData") {

    @Serializable
    data class Data(
        @Comment("当前机器人的黑名单 QQ 列表，处于 QQ 黑名单内的成员不能执行指令，且会被自动拒绝加群。")
        var commandBlackList: List<Long> = mutableListOf(),

        @Comment("聊天敏感词过滤器（中文）。")
        var inappropriateWordsCN: MutableList<String> = mutableListOf(),

        @Comment("聊天敏感词过滤器（英文）。")
        var inappropriateWordsEN: MutableList<String> = mutableListOf()
    )

    private val botDataList : Map<Long, Data> by value(
        mutableMapOf(
            1234567890L to Data(
                commandBlackList = mutableListOf(),
                inappropriateWordsCN = mutableListOf(),
                inappropriateWordsEN = mutableListOf()
            ),
            9876543210L to Data(
                commandBlackList = mutableListOf(),
                inappropriateWordsCN = mutableListOf(),
                inappropriateWordsEN = mutableListOf()
            ),
        )
    )

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