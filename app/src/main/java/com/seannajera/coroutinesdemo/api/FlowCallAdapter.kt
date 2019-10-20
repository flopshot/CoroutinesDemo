package com.seannajera.coroutinesdemo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapter<R>(private val responseType: Type) : CallAdapter<R, Any> {

    override fun responseType(): Type = responseType

    @Throws(IllegalStateException::class)
    override fun adapt(call: Call<R>): Any = flow {

        try {
            val r = call.execute()
            r.let { response ->
                if (response.isSuccessful) {
                    response.body()?.apply {
                        emit(this)
                    } ?: kotlin.run {
                        throw IllegalStateException("Body Is Empty or Null")
                    }
                } else {
                    throw IllegalStateException("Unsuccessful Response")
                }
            }
        } catch (e: IOException) {
            throw IllegalStateException("Bad Network State")
        }
    }
}

class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): CallAdapter<*, *>? {
        return returnType?.let {
            return try {
                // get enclosing type
                val enclosingType = (it as ParameterizedType)

                // ensure enclosing type is 'Simple'
                if (enclosingType.rawType != Flow::class.java)
                    null
                else {
                    val type = enclosingType.actualTypeArguments[0]
                    FlowCallAdapter<Any>(type)
                }
            } catch (ex: ClassCastException) {
                null
            }
        }
    }

    companion object {
        @JvmStatic
        fun create() = FlowCallAdapterFactory()
    }
}