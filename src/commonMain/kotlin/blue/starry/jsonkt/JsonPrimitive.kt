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

package blue.starry.jsonkt

import kotlinx.serialization.json.*

inline fun <reified T> JsonPrimitive.cast(): T {
    return safeCast<T>() ?: throw IllegalArgumentException("${T::class.simpleName} is not primitive type.")
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> JsonPrimitive.safeCast(): T? {
    return when (T::class) {
        Boolean::class -> {
            booleanOrNull
        }
        Int::class -> {
            intOrNull
        }
        Long::class -> {
            longOrNull
        }
        Float::class -> {
            floatOrNull
        }
        Double::class -> {
            doubleOrNull
        }
        Char::class -> {
            contentOrNull?.firstOrNull()
        }
        String::class -> {
            contentOrNull
        }
        else -> {
            null
        }
    } as T?
}

/*
 * toJsonPrimitive
 */

/**
 * @throws JsonCastException
 */
fun String.toJsonPrimitive(): JsonPrimitive {
    return toJsonElement().cast()
}

fun String?.toJsonPrimitiveOrNull(): JsonPrimitive? {
    return runSafely {
        toJsonPrimitive()
    }
}


/*
 * isXXX
 */

inline val JsonPrimitive.isBoolean: Boolean
    get() = booleanOrNull != null

inline val JsonPrimitive.isInt: Boolean
    get() = intOrNull != null

inline val JsonPrimitive.isLong: Boolean
    get() = longOrNull != null

inline val JsonPrimitive.isFloat: Boolean
    get() = floatOrNull != null

inline val JsonPrimitive.isDouble: Boolean
    get() = doubleOrNull != null
