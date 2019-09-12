/*
 * The MIT License (MIT)
 *
 *     Copyright (c) 2017-2019 Nephy Project Team
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

package jp.nephy.jsonkt.delegation

import jp.nephy.jsonkt.JsonObject

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListOrDefaultProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListOrNullProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T?>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListOrDefaultProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListOrNullProperty(key: String? = null, vararg parameters: Any?): JsonArrayProperty<T?>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListProperty(key: String? = null): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListOrDefaultProperty(key: String? = null): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonObject?.jsonModelListOrNullProperty(key: String? = null): JsonArrayProperty<T?>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListProperty(key: String? = null): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListOrDefaultProperty(key: String? = null): JsonArrayProperty<T>

@PublishedApi
internal expect inline fun <reified T: JsonModel> JsonModel?.jsonModelListOrNullProperty(key: String? = null): JsonArrayProperty<T?>

/*
    List<JsonModel>
 */

inline fun <reified T: JsonModel> JsonObject.byModelList(key: String? = null, vararg parameters: Any?) = jsonModelListProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonModel.modelList(key: String? = null, vararg parameters: Any?) = jsonModelListProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonObject.byModelListOrDefault(key: String? = null, vararg parameters: Any?) = jsonModelListOrDefaultProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonModel.modelListOrDefault(key: String? = null, vararg parameters: Any?) = jsonModelListOrDefaultProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonObject.byModelList(key: String? = null) = jsonModelListProperty<T>(key)

inline fun <reified T: JsonModel> JsonModel.modelList(key: String? = null) = jsonModelListProperty<T>(key)

inline fun <reified T: JsonModel> JsonObject.byModelListOrDefault(key: String? = null) = jsonModelListOrDefaultProperty<T>(key)

inline fun <reified T: JsonModel> JsonModel.modelListOrDefault(key: String? = null) = jsonModelListOrDefaultProperty<T>(key)

/*
    List<JsonModel?>
 */

inline fun <reified T: JsonModel> JsonObject?.byNullableModelList(key: String? = null, vararg parameters: Any?) = jsonModelListOrNullProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonModel?.nullableModelList(key: String? = null, vararg parameters: Any?) = jsonModelListOrNullProperty<T>(key, *parameters)

inline fun <reified T: JsonModel> JsonObject?.byNullableModelList(key: String? = null) = jsonModelListOrNullProperty<T>(key)

inline fun <reified T: JsonModel> JsonModel?.nullableModelList(key: String? = null) = jsonModelListOrNullProperty<T>(key)
