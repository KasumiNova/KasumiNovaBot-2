package github.kasuminova.kasuminovabot.module.tips

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomTipHelper {

    @JvmStatic
    fun parseFromJSONString() {

    }

    @JvmStatic
    fun toJSONString(tip: CustomTip) {
        val tips = listOf(
            tip
        )
        val json = Json.encodeToString(tips)

    }

}