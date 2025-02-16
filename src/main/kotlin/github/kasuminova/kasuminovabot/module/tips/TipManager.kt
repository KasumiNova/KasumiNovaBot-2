package github.kasuminova.kasuminovabot.module.tips

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.TreeMap
import java.util.concurrent.ThreadLocalRandom

object TipManager {

    @JvmStatic
    private val JSON = Json { prettyPrint = true }

    @JvmStatic
    var tipStorage: TipStorage = TipStorage()

    @JvmStatic
    val indexedTips: MutableList<CustomTip> = mutableListOf()

    @JvmStatic
    var changed: Boolean = false

    @Serializable
    data class TipStorage(
        val checkingTips: MutableMap<Int, CustomTip> = TreeMap(),
        val checkedTips: MutableMap<Int, CustomTip> = TreeMap(),
        var currentId: Int = 0,
    )

    @JvmStatic
    fun randomTip(): CustomTip? {
        if (indexedTips.isEmpty()) {
            return null;
        }
        val idx = ThreadLocalRandom.current().nextInt(0, indexedTips.size)
        return indexedTips[idx]
    }

    @JvmStatic
    fun addCheckingTip(tip: CustomTip) {
        changed = true
        tipStorage.checkingTips[tip.id] = tip
    }

    @JvmStatic
    fun addCheckedTip(tip: CustomTip) {
        changed = true
        tipStorage.checkedTips[tip.id] = tip

        indexedTips.clear()
        tipStorage.checkedTips.forEach { (_, u) -> indexedTips.add(u) }
    }

    @JvmStatic
    fun removeTip(id: Int): Boolean {
        changed = true
        return when {
            tipStorage.checkingTips.remove(id) != null -> true
            tipStorage.checkedTips.remove(id) != null -> true
            else -> false
        }
    }

    @JvmStatic
    fun approveTips(id: Int): Boolean {
        changed = true
        val tip = tipStorage.checkingTips[id]
        if (tip != null) {
            tipStorage.checkingTips.remove(id)
            tipStorage.checkedTips[id] = tip
            indexedTips.clear()
            tipStorage.checkedTips.forEach { (_, u) -> indexedTips.add(u) }
            return true
        }
        return false
    }

    @JvmStatic
    @Synchronized
    fun nextId(): Int {
        changed = true
        return tipStorage.currentId++
    }

    @JvmStatic
    fun decodeJsonData(jsonStr: String) {
        changed = false
        try {
            tipStorage = Json.decodeFromString<TipStorage>(jsonStr)
            indexedTips.clear()
            tipStorage.checkedTips.forEach { (_, u) -> indexedTips.add(u) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun encodeToJsonString(): String {
        return JSON.encodeToString(tipStorage)
    }

}