package github.kasuminova.kasuminovabot.module.serverhelper.command

import github.kasuminova.kasuminovabot.KasumiNovaBot2
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL
import github.kasuminova.kasuminovabot.util.FileUtil
import github.kasuminova.kasuminovabot.util.MiraiCodes
import github.kasuminova.kasuminovabot.util.MiscUtil
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.QuoteReply
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class WhiteListScanCmd(cl: ServerHelperCL?) : GroupCommandCL(
    "白名单扫描",
    0,
    3,
    MessageChainBuilder()
        .append("扫描整个群聊的所有成员，并导出所有匹配与不匹配的成员信息至后台文件。").append(MiraiCodes.WRAP)
        .append("仅限机器人管理员使用。")
        .build(),
    cl
) {
    override fun execute(event: GroupMessageEvent, args: List<String>) {
        ServerHelperCL.EXECUTOR.execute { execute0(event) }
    }

    @Serializable
    data class ToSerialize(val onWhiteListMembers: Map<Long, Map<String, String>>,
                           val notOnWhiteListMembers: Map<Long, Map<String, String>>)

    companion object {
        private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
        private val FILE_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss")

        private fun execute0(event: GroupMessageEvent) {
            val start = System.currentTimeMillis()
            val currentTimeStamp = TimeUnit.MILLISECONDS.toSeconds(start)
            val group = event.group
            val members = group.members
            val fileName = "whitelist_" + group.id + ".json"
            val file = KasumiNovaBot2.INSTANCE.resolveDataFile(fileName)
            if (!file.exists()) {
                MiscUtil.sendMessageToGroup(
                    MessageChainBuilder()
                        .append(QuoteReply(event.message))
                        .append("无法找到白名单数据文件，请确认后台是否存在相关文件。").append(MiraiCodes.WRAP)
                        .append("文件名：").append(fileName)
                        .build(),
                    event.group
                )
                return
            }

            val onWhiteListMembers: MutableMap<Long, Map<String, String>> = ConcurrentHashMap()
            val notOnWhiteListMembers: MutableMap<Long, Map<String, String>> = ConcurrentHashMap()
            val jsonObject: JsonObject = getWhiteListJsonObject(event, file, fileName) ?: return
            var neverSpokenMembers = 0
            var divedOver6MonthMembers = 0

            members.parallelStream().forEach { member ->
                val id = member.id
                val memberInfo: MutableMap<String, String> = HashMap()

                val joinTimestamp = member.joinTimestamp.toLong()
                val lastSpeakTimestamp = member.lastSpeakTimestamp.toLong()

                memberInfo["joinTime"] = getFormattedDateTime(joinTimestamp)
                memberInfo["lastSpeakTime"] = getFormattedDateTime(lastSpeakTimestamp)

                val neverSpoken = joinTimestamp == lastSpeakTimestamp || (
                        joinTimestamp < (currentTimeStamp - 3 * 24 * 60 * 60) && (lastSpeakTimestamp - joinTimestamp) <= 3 * 24 * 60 * 60)
                val divedOver6Months = joinTimestamp <= currentTimeStamp - 6 * 30 * 24 * 60 * 60
                        && (lastSpeakTimestamp - joinTimestamp) <= 6 * 30 * 24 * 60 * 60

                val jsonElement = jsonObject[id.toString()]
                if (jsonElement is JsonObject) {
                    try {
                        val lastUpdateTime = jsonElement["lastUpdateTime"].toString().toLong()
                        memberInfo["whiteListId"] = jsonElement["userName"].toString().removeSurrounding("\"")
                        memberInfo["whiteListRegisterTime"] = formatDateTime(lastUpdateTime)
                        memberInfo["isDivedOver6MonthsAndHasWhiteList"] = if (divedOver6Months) { "true" } else { "false" }
                        memberInfo["isNeverSpokenAndHasWhiteList"] = if (neverSpoken) {
                            neverSpokenMembers++
                            "true"
                        } else {
                            "false"
                        }
                        memberInfo["isDivedOver6MonthsAndHasWhiteList"] = if (divedOver6Months) {
                            divedOver6MonthMembers++
                            "true"
                        } else {
                            "false"
                        }
                    } catch (e: NullPointerException) {
                        KasumiNovaBot2.INSTANCE.logger.warning("成员：$id 的白名单信息存在问题，已停止导入该成员的信息。")
                        return@forEach
                    }
                    onWhiteListMembers[id] = memberInfo
                } else {
                    memberInfo["isNeverSpokenAndHasNoWhiteList"] = if (neverSpoken) {
                        neverSpokenMembers++
                        "true"
                    } else {
                        "false"
                    }
                    memberInfo["isDivedOver6MonthsAndHasNoWhiteList"] = if (divedOver6Months) {
                        divedOver6MonthMembers++
                        "true"
                    } else {
                        "false"
                    }
                    notOnWhiteListMembers[id] = memberInfo
                }
            }

            onWhiteListMembers.toList().sortedBy { it.second["lastSpeakTime"] }.toMap()

            val jsonStr = Json.encodeToString(ToSerialize(onWhiteListMembers, notOnWhiteListMembers))
            val scanResultFileName = "scan_result_" + formatFileDateTime(System.currentTimeMillis()) + ".json"
            val scanResultFile = KasumiNovaBot2.INSTANCE.resolveDataFile(scanResultFileName)
            try {
                assert(!scanResultFile.exists() || scanResultFile.delete())
                assert(scanResultFile.createNewFile())
                val raf = RandomAccessFile(scanResultFile, "rws")
                FileUtil.writeStringToFile(raf, jsonStr)
                raf.close()
            } catch (e: Exception) {
                MiscUtil.sendMessageToGroup(
                    MessageChainBuilder()
                        .append(QuoteReply(event.message))
                        .append("无法正常输出结果文件，请检查后台文件是否已存在或正在被占用。").append(MiraiCodes.WRAP)
                        .append("文件名：").append(scanResultFileName)
                        .build(),
                    event.group
                )
                return
            }

            MiscUtil.sendMessageToGroup(
                MessageChainBuilder()
                    .append(QuoteReply(event.message))
                    .append("检查分析完毕，请前往后台查看。")
                    .append("（耗时：").append((System.currentTimeMillis() - start).toString()).append("ms）")
                    .append(MiraiCodes.WRAP).append("分析结果简介：")
                    .append(MiraiCodes.WRAP).append("有效白名单总数：").append(jsonObject.size.toString())
                    .append(MiraiCodes.WRAP).append("位于群内的白名单玩家：").append(onWhiteListMembers.size.toString())
                    .append(String.format("（%.1f%%）",
                        (onWhiteListMembers.size.toFloat() / members.size.toFloat()) * 100F))
                    .append(MiraiCodes.WRAP).append("无白名单群员数量：").append(notOnWhiteListMembers.size.toString())
                    .append(String.format("（%.1f%%）",
                        (notOnWhiteListMembers.size.toFloat() / members.size.toFloat()) * 100F))
                    .append(MiraiCodes.WRAP).append("白名单玩家群内留存比例：")
                    .append(String.format("%.1f",
                        (onWhiteListMembers.size.toFloat() / jsonObject.size.toFloat()) * 100F)).append("%")
                    .append(MiraiCodes.WRAP).append("潜水成员数量（超过 6 个月）：").append(divedOver6MonthMembers.toString())
                    .append(String.format("（%.1f%%）", (divedOver6MonthMembers / members.size.toFloat()) * 100F))
                    .append(MiraiCodes.WRAP).append("异常成员数量（加群从未发言或发言数极少）：").append(neverSpokenMembers.toString())
                    .append(String.format("（%.1f%%）", (neverSpokenMembers / members.size.toFloat()) * 100F))
                    .append(MiraiCodes.WRAP).append("文件结果已导出至后台，文件名：").append(scanResultFileName)
                    .build(),
                event.group
            )
        }

        private fun formatDateTime(time: Long): String {
            val from = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
            return from.format(DATE_TIME_FORMATTER)
        }

        private fun formatFileDateTime(time: Long): String {
            val from = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
            return from.format(FILE_DATE_TIME_FORMATTER)
        }

        private fun getFormattedDateTime(time: Long): String {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault())
            return dateTime.format(DATE_TIME_FORMATTER)
        }

        private fun getWhiteListJsonObject(
            event: GroupMessageEvent,
            file: File,
            fileName: String
        ): JsonObject? {
            var jsonObject: JsonObject? = null
            try {
                val jsonElement = Json.parseToJsonElement(FileUtil.readStringFromFile(file))
                if (jsonElement is JsonObject) {
                    jsonObject = jsonElement
                } else {
                    throw IOException()
                }
            } catch (e: IOException) {
                MiscUtil.sendMessageToGroup(
                    MessageChainBuilder()
                        .append(QuoteReply(event.message))
                        .append("无法正常读取白名单数据文件，请确认文件是否损坏或格式是否正确。").append(MiraiCodes.WRAP)
                        .append("文件名：").append(fileName)
                        .build(),
                    event.group
                )
            }
            return jsonObject
        }
    }
}
