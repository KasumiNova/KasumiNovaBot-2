package github.kasuminova.kasuminovabot.module.entertainment.hitokoto

import github.kasuminova.kasuminovabot.module.entertainment.config.hitokoto.HitokotoResult
import kotlinx.serialization.json.*

object HitokotoDeserializer {
    @JvmStatic
    fun deserializeHitokoto(jsonString: String) : HitokotoResult? {
        val json = Json
        val jsonElement = json.parseToJsonElement(jsonString);
        if (jsonElement !is JsonObject) {
            return null;
        }

        val result = HitokotoResult()
        val jsonObj : JsonObject = jsonElement;

        result.setId(getJsonNodeInt(jsonObj, "id"))
            .setUUID(getJsonNodeString(jsonObj, "uuid"))
            .setHitokoto(getJsonNodeString(jsonObj, "hitokoto"))
            .setType(getJsonNodeString(jsonObj, "type"))
            .setFrom(getJsonNodeString(jsonObj, "from"))
            .setFromWho(getJsonNodeString(jsonObj, "from_who"))
            .setCreator(getJsonNodeString(jsonObj, "creator"))
            .setCreatorUid(getJsonNodeInt(jsonObj, "creator_uid"))
            .setReviewer(getJsonNodeInt(jsonObj, "reviewer"))
            .setCommitFrom(getJsonNodeString(jsonObj, "commit_from"))
            .setCreatedAt(getJsonNodeString(jsonObj, "created_at"))
            .setLength(getJsonNodeInt(jsonObj, "length"))

        return result
    }

    private fun getJsonNodeInt(json: JsonObject, memberName: String): Int {
        val element: JsonPrimitive = json.getValue(memberName).jsonPrimitive
        return element.intOrNull ?: -1
    }

    private fun getJsonNodeString(json: JsonObject, memberName: String): String {
        val element: JsonPrimitive = json.getValue(memberName).jsonPrimitive
        return if (element.isString) {
            element.content
        } else ""
    }
}
