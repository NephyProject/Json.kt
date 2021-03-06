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

@file:Suppress("UNUSED", "DEPRECATION")

package blue.starry.jsonkt.delegation

import blue.starry.jsonkt.JsonKtException
import blue.starry.jsonkt.JsonObject
import blue.starry.jsonkt.cast
import blue.starry.jsonkt.safeCast
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.KClass

@Deprecated("Directly implementing JsonEnum<T> is deprecated for type safety. Use {BooleanJsonEnum, IntJsonEnum, LongJsonEnum, FloatJsonEnum, DoubleJsonEnum, CharJsonEnum, StringJsonEnum} instead of JsonEnum<T>.")
interface JsonEnum<T: Any> {
    val value: T
}

interface BooleanJsonEnum: JsonEnum<Boolean>
interface IntJsonEnum: JsonEnum<Int>
interface LongJsonEnum: JsonEnum<Long>
interface FloatJsonEnum: JsonEnum<Float>
interface DoubleJsonEnum: JsonEnum<Double>
interface CharJsonEnum: JsonEnum<Char>
interface StringJsonEnum: JsonEnum<String>

class NoSuchEnumMemberException(
    @Suppress("UNUSED_PARAMETER") val enumClass: KClass<*>,
    @Suppress("UNUSED_PARAMETER") val value: Any
): JsonKtException("Enum class ${enumClass.simpleName} does not have member of value $value.")

@PublishedApi
internal inline fun <T: Any, reified E> findEnumMember(value: T): E where E: Enum<E>, E: JsonEnum<T> {
    return findEnumMemberOrNull(value) ?: throw NoSuchEnumMemberException(E::class, value)
}

@PublishedApi
internal inline fun <T: Any, reified E> findEnumMemberOrNull(value: T?): E? where E: Enum<E>, E: JsonEnum<T> {
    if (value == null) {
        return null
    }

    return enumValues<E>().find { const -> const.value == value }
}

/*
    JsonEnum
 */

inline fun <reified T: Any, reified E> JsonObject.byEnum(
    key: String? = null,
    noinline default: JsonObjectDefaultSelector<E> = ::jsonObjectDefaultSelector
): JsonObjectProperty<E> where E: Enum<E>, E: JsonEnum<T> = jsonObjectProperty(key, default) {
    val casted = it.jsonPrimitive.cast<T>()
    
    findEnumMember(casted)
}

inline fun <reified T: Any, reified E> JsonModel.enum(
    key: String? = null,
    noinline default: JsonObjectDefaultSelector<E> = ::jsonObjectDefaultSelector
): JsonObjectProperty<E> where E: Enum<E>, E: JsonEnum<T> = jsonObjectProperty(key, default) {
    val casted = it.jsonPrimitive.cast<T>()

    findEnumMember(casted)
}

/*
    JsonEnum?
 */

inline fun <reified T: Any, reified E> JsonObject?.byNullableEnum(
    key: String? = null,
    noinline default: JsonObjectDefaultSelector<E?> = ::jsonObjectDefaultSelectorWithNull
): JsonObjectProperty<E?> where E: Enum<E>, E: JsonEnum<T> = nullableJsonObjectProperty(key, default) {
    val casted = it.jsonPrimitive.safeCast<T>()
    
    findEnumMemberOrNull(casted)
}

inline fun <reified T: Any, reified E> JsonModel?.nullableEnum(
    key: String? = null,
    noinline default: JsonObjectDefaultSelector<E?> = ::jsonObjectDefaultSelectorWithNull
): JsonObjectProperty<E?> where E: Enum<E>, E: JsonEnum<T> = nullableJsonObjectProperty(key, default) {
    val casted = it.jsonPrimitive.safeCast<T>()

    findEnumMemberOrNull(casted)
}

/*
    List<JsonEnum>
 */

inline fun <reified T: Any, reified E> JsonObject.byEnumList(
    key: String? = null,
    noinline default: JsonArrayDefaultSelector<E> = ::jsonArrayDefaultSelector
): JsonArrayProperty<E> where E: Enum<E>, E: JsonEnum<T> = jsonArrayProperty(key, default) { json ->
    val casted = json.jsonPrimitive.cast<T>()

    findEnumMember(casted)
}

inline fun <reified T: Any, reified E> JsonModel.enumList(
    key: String? = null,
    noinline default: JsonArrayDefaultSelector<E> = ::jsonArrayDefaultSelector
): JsonArrayProperty<E> where E: Enum<E>, E: JsonEnum<T> = jsonArrayProperty(key, default) {
    val casted = it.jsonPrimitive.cast<T>()

    findEnumMember(casted)
}

/*
    List<JsonEnum?>
 */

inline fun <reified T: Any, reified E> JsonObject?.byNullableEnumList(
    key: String? = null,
    noinline default: JsonArrayDefaultSelector<E?> = ::jsonArrayDefaultSelector
): JsonArrayProperty<E?> where E: Enum<E>, E: JsonEnum<T> = nullableJsonArrayProperty(key, default) {
    val casted = it.jsonPrimitive.safeCast<T>()

    findEnumMemberOrNull(casted)
}

inline fun <reified T: Any, reified E> JsonModel?.nullableEnumList(
    key: String? = null,
    noinline default: JsonArrayDefaultSelector<E?> = ::jsonArrayDefaultSelector
): JsonArrayProperty<E?> where E: Enum<E>, E: JsonEnum<T> = nullableJsonArrayProperty(key, default) {
    val casted = it.jsonPrimitive.safeCast<T>()

    findEnumMemberOrNull(casted)
}
