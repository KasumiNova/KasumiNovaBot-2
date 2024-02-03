package github.kasuminova.kasuminovabot.module.serverhelper.hypernet

import kotlinx.serialization.json.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("unused")
class ResearchCognitionData(
    val researchName: String,
    val techLevel: Float,
    val requiredPoints: Double,
    val minComputationPointPerTick: Float,
    val descriptions: MutableList<String>,
    val unlockedDescriptions: MutableList<String>,
    val dependencies: MutableList<String>
) {
    override fun hashCode(): Int {
        return researchName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is ResearchCognitionData) {
            return false
        }
        return researchName == other.researchName
    }

    companion object {
        fun parseFromJsonObject(jsonObject: JsonObject): ResearchCognitionData {
            val researchName : String = jsonObject.getValue("researchName").jsonPrimitive.content
            val techLevel : Float = jsonObject.getValue("techLevel").jsonPrimitive.float

            val requiredPoints : Double = jsonObject.getValue("requiredPoints").jsonPrimitive.double
            val minComputationPointPerTick : Float = jsonObject.getValue("minComputationPointPerTick").jsonPrimitive.float

            val descriptions : MutableList<String> = jsonObject.getValue("descriptions").jsonArray.let { array ->
                val list : MutableList<String> = ArrayList()
                array.forEach { list.add(it.jsonPrimitive.content) }
                list
            }

            val unlockedDescriptions : MutableList<String> = jsonObject.getValue("unlockedDescriptions").jsonArray.let { array ->
                val list : MutableList<String> = ArrayList()
                array.forEach { list.add(it.jsonPrimitive.content) }
                list
            }

            val dependencies : MutableList<String> = jsonObject.getValue("dependencies").jsonArray.let { array ->
                val list : MutableList<String> = ArrayList()
                array.forEach { list.add(it.jsonPrimitive.content) }
                list
            }

            return ResearchCognitionData(
                researchName,
                techLevel,
                requiredPoints,
                minComputationPointPerTick,
                descriptions,
                unlockedDescriptions,
                dependencies
            )
        }
    }
}
