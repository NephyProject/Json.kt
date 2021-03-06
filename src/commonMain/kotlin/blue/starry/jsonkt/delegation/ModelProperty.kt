/*
 * The MIT License (MIT)
 *
 *     Copyright (c) 2017-2020 StarryBlueSky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("UNUSED")

package blue.starry.jsonkt.delegation

import blue.starry.jsonkt.JsonObject
import blue.starry.jsonkt.jsonObjectOf
import kotlinx.serialization.json.jsonObject

@PublishedApi
internal fun <T: JsonModel> JsonObject.jsonModelProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> {
    return jsonObjectProperty(key) {
        block(it.jsonObject)
    }
}

@PublishedApi
internal fun <T: JsonModel> JsonObject.jsonModelOrDefaultProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> {
    return jsonObjectProperty(key) {
        runCatching {
            block(it.jsonObject)
        }.recoverCatching {
            block(jsonObjectOf())
        }.getOrThrow()
    }
}

@PublishedApi
internal fun <T: JsonModel> JsonObject?.jsonModelOrNullProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T?> {
    return nullableJsonObjectProperty(key) {
        block(it.jsonObject)
    }
}

@PublishedApi
internal fun <T: JsonModel> JsonModel.jsonModelProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> {
    return jsonObjectProperty(key) { 
        block(it.jsonObject)
    }
}

@PublishedApi
internal fun <T: JsonModel> JsonModel.jsonModelOrDefaultProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> {
    return jsonObjectProperty(key) { 
        runCatching { 
            block(it.jsonObject)
        }.recoverCatching { 
            block(jsonObjectOf())
        }.getOrThrow()
    }
}

@PublishedApi
internal fun <T: JsonModel> JsonModel?.jsonModelOrNullProperty(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T?> {
    return nullableJsonObjectProperty(key) { 
        block(it.jsonObject)
    }
}

/*
    JsonModel
 */

fun <T: JsonModel> JsonObject.byModel(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> = jsonModelProperty(key, block)

fun <T: JsonModel> JsonModel.model(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> = jsonModelProperty(key, block)

fun <T: JsonModel> JsonObject.byModelOrDefault(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> = jsonModelOrDefaultProperty(key, block)

fun <T: JsonModel> JsonModel.modelOrDefault(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T> = jsonModelOrDefaultProperty(key, block)

/*
    JsonModel?
 */

fun <T: JsonModel> JsonObject?.byNullableModel(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T?> = jsonModelOrNullProperty(key, block)

fun <T: JsonModel> JsonModel?.nullableModel(key: String? = null, block: (JsonObject) -> T): JsonObjectProperty<T?> = jsonModelOrNullProperty(key, block)
