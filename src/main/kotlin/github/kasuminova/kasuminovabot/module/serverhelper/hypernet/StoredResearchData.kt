package github.kasuminova.kasuminovabot.module.serverhelper.hypernet

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/**
 * Example:
 * `{
 * "dataList": [
 * {
 * "researchName": "name",
 * "techLevel": 1.0,
 * "requiredPoints": 100.0,
 * "minComputationPointPerTick": 0.5,
 * "descriptions": [
 * "desc1",
 * "desc2"
 * ],
 * "unlockedDescriptions": [
 * "unlockedDesc1",
 * "unlockedDesc2"
 * ],
 * "dependencies": [
 * "dep1",
 * "dep2"
 * ]
 * }
 * ]
 * }
` *
 */
class StoredResearchData {
    val dataList: MutableList<ResearchCognitionData> = ArrayList()

    companion object {
        fun parseFromJSONString(jsonString: String): StoredResearchData {
            val json = Json
            val jsonElement = json.parseToJsonElement(jsonString);
            val stored = StoredResearchData()
            if (jsonElement !is JsonObject) {
                return stored
            }

            val jsonObj : JsonObject = jsonElement;
            jsonObj["dataList"]?.jsonArray?.forEach { data ->
                if (data is JsonObject) {
                    stored.dataList.add(ResearchCognitionData.parseFromJsonObject(data))
                }
            }

            return stored
        }
    }
}
