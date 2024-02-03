package github.kasuminova.kasuminovabot.module.tips

import kotlinx.serialization.Serializable

@Serializable
data class CustomTip(
    val id: Int,
    val tip: String,
    val submitter: Long,
    val who: String,
    val createTime: Long,
)
