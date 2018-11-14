@file:Suppress("UNUSED")

package jp.nephy.jsonkt.cli

import jp.nephy.jsonkt.cli.property.*
import jp.nephy.jsonkt.jsonObjectOf
import jp.nephy.jsonkt.toJsonObject
import kotlinx.serialization.json.*
import java.io.Closeable
import java.io.File
import java.nio.file.Path
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

fun generateModelClass(): String {
    print("Model name? (Optional): ")
    val modelName = readLine().orEmpty()
    print("Use type strict mode? (Y/n): ")
    val printComments = readLine().orEmpty().toLowerCase() == "y"
    println("Input json string. If blank line is input, quit.")

    while (true) {
        var text = ""
        while (true) {
            val line = readLine()
            if (line.isNullOrBlank()) {
                break
            }
            text += line
        }

        try {
            return text.toModelString(modelName, printComments)
        } catch (e: Throwable) {
            System.err.write("Invalid json format: ${e.localizedMessage}\n".toByteArray())
            continue
        }
    }
}

/*
 * toModelString
 */

fun JsonObject.toModelString(modelName: String? = null, printComments: Boolean? = null): String {
    return JsonToKotlinClass(this).convert(modelName, printComments)
}

fun String.toModelString(modelName: String? = null, printComments: Boolean? = null): String {
    return JsonToKotlinClass(toJsonObject()).convert(modelName, printComments)
}

fun File.toModelString(modelName: String? = null, printComments: Boolean? = null): String {
    return toJsonObject().toModelString(modelName, printComments)
}

fun Path.toModelString(modelName: String? = null, printComments: Boolean? = null): String {
    return toFile().toModelString(modelName, printComments)
}

class JsonToKotlinClass internal constructor(private val json: JsonObject) {
    fun convert(targetModelName: String?, printComments: Boolean?): String {
        return buildString {
            appendln("import jp.nephy.jsonkt.*")
            appendln("import jp.nephy.jsonkt.delegation.*\n")

            JsonObjectParser(json).use {
                val modelName = targetModelName.orEmpty().ifBlank { "Model" }
                append(it.toModelString(modelName, printComments ?: true))
            }
        }.trimEnd()
    }

    private class JsonObjectParser(private val json: JsonObject): Closeable {
        fun toModelString(name: String, printComments: Boolean): String {
            return buildString {
                val subModels = mutableListOf<String>()
                append("data class $name(override val json: JsonObject): JsonModel {\n")
                json.map { pair ->
                    val (key, value) = pair
                    when (value) {
                        is JsonObject -> when {
                            value.toString() == "{}" -> JsonObjectProperty(pair, printComments)
                            value.isNullable -> JsonNullableModelProperty(pair, printComments)
                            else -> JsonModelProperty(pair, printComments)
                        }
                        is JsonArray -> when {
                            value.isEmpty() || value.all { element -> element is JsonArray } -> JsonArrayProperty(pair, printComments)
                            value.all { element -> element is JsonObject } -> when {
                                value.isNullable -> JsonNullableModelListProperty(pair, printComments)
                                else -> JsonModelListProperty(pair, printComments)
                            }
                            value.all { element -> element is JsonPrimitive } -> JsonPrimitiveListProperty(pair, printComments)
                            else -> throw IllegalStateException("Not all elements in array are same type. These must be JsonObject, JsonArray or JsonPrimitive. ($key: $value)")
                        }
                        is JsonNull -> JsonNullProperty(pair, printComments)
                        is JsonPrimitive -> when {
                            value.isNullable -> JsonNullablePrimitiveProperty(pair, printComments)
                            else -> JsonPrimitiveProperty(pair, printComments)
                        }
                        else -> throw IllegalArgumentException("Unknown type: $value")
                    }
                }.sortedBy { it.key }.forEach {
                    if (it is JsonModelProperty) {
                        val parser = JsonObjectParser(it.element.jsonObject)
                        subModels.add(parser.toModelString(it.modelName, printComments))
                    } else if (it is JsonModelListProperty) {
                        val values = mutableMapOf<String, MutableSet<JsonElement>>()
                        it.element.jsonArray.forEach { element ->
                            element.jsonObject.forEach { k, v ->
                                if (k in values) {
                                    values[k]?.add(v)
                                } else {
                                    values[k] = mutableSetOf(v)
                                }
                            }
                        }

                        val altJson = jsonObjectOf(*values.map { (k, v) ->
                            when {
                                v.all { element -> !element.isNull } -> k to v.run {
                                    if (all { element -> element is JsonObject }) {
                                        val innerValues = mutableMapOf<String, Pair<MutableSet<JsonElement>, Boolean>>()
                                        forEach { element ->
                                            element.jsonObject.forEach { k, v ->
                                                if (k in innerValues) {
                                                    innerValues[k]?.first?.add(v)
                                                } else {
                                                    innerValues[k] = mutableSetOf(v) to (size != count { element -> k in element.jsonObject })
                                                }
                                            }
                                        }

                                        jsonObjectOf(*innerValues.map { (k, v) ->
                                            k to v.run {
                                                first.first().apply {
                                                    if (second) {
                                                        when (this) {
                                                            is JsonPrimitive -> isNullable = true
                                                            is JsonObject -> isNullable = true
                                                            is JsonArray -> isNullable = true
                                                        }
                                                    }
                                                }
                                            }
                                        }.toTypedArray())
                                    } else {
                                        first()
                                    }
                                }
                                v.all { element -> element.isNull } -> k to JsonNull
                                else -> k to v.find { element -> !element.isNull }!!.apply {
                                    primitive.isNullable = true
                                }
                            }
                        }.toTypedArray())

                        val parser = JsonObjectParser(altJson)
                        subModels.add(parser.toModelString(it.modelName, printComments))
                    }
                    append("    ${it.toPropertyString()}\n")
                }
                append("}\n\n")

                subModels.forEach {
                    append("$it\n")
                }
            }.replace("\n\n\n", "\n\n")
        }

        override fun close() {
            nullablePrimitiveCache.clear()
            nullableObjectCache.clear()
            nullableArrayCache.clear()
        }
    }
}

private val nullablePrimitiveCache = mutableMapOf<JsonPrimitive, Boolean>()
private var JsonPrimitive.isNullable: Boolean
    get() = nullablePrimitiveCache[this] ?: false
    set(value) = nullablePrimitiveCache.set(this, value)

private val nullableObjectCache = mutableMapOf<JsonObject, Boolean>()
private var JsonObject.isNullable: Boolean
    get() = nullableObjectCache[this] ?: false
    set(value) = nullableObjectCache.set(this, value)

private val nullableArrayCache = mutableMapOf<JsonArray, Boolean>()
private var JsonArray.isNullable: Boolean
    get() = nullableArrayCache[this] ?: false
    set(value) = nullableArrayCache.set(this, value)
