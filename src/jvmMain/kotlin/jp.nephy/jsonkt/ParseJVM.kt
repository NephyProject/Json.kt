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

@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package jp.nephy.jsonkt

import jp.nephy.jsonkt.delegation.JsonModel
import kotlin.reflect.KClass

/*
 * parse
 */

actual inline fun <T: JsonModel> JsonElement.parseAs(model: KClass<T>, vararg parameters: Any?): T {
    return try {
        model.createModelInstance(jsonObject, *parameters)
    } catch (e: Throwable) {
        throw InvalidJsonModelException(model)
    }
}

actual inline fun <T: JsonModel> JsonElement.parseAs(model: KClass<T>): T {
    return try {
        model.createModelInstance(jsonObject)
    } catch (e: Throwable) {
        throw InvalidJsonModelException(model)
    }
}

/*
 * parseList
 */

actual inline fun <T: JsonModel> JsonElement.parseListAs(model: KClass<T>, vararg parameters: Any?): List<T> {
    return try {
        jsonArray.map {
            it.parseAs(model, *parameters)
        }
    } catch (e: Throwable) {
        throw InvalidJsonModelException(model)
    }
}

actual inline fun <T: JsonModel> JsonElement.parseListAs(model: KClass<T>): List<T> {
    return try {
        jsonArray.map {
            it.parseAs(model)
        }
    } catch (e: Throwable) {
        throw InvalidJsonModelException(model)
    }
}
