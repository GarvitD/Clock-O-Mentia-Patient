package com.example.clock_o_mentiapatient.models

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T> (
    var networkState: NetworkState,   // To check network state
    var code: Int = networkState.code,

    var message: String? = null,

    @SerializedName("result")
    var data: T? = null
) {
    companion object {

        fun <T> success(responseWrapper: ResponseWrapper<T>): ResponseWrapper<T> {
            responseWrapper.networkState = NetworkState.SUCCESS
            return responseWrapper
        }

        fun <T> error(msg: String? = null, data: T? = null): ResponseWrapper<T> {
            return ResponseWrapper(networkState = NetworkState.ERROR, message = msg, data = data)
        }

        fun <T> loading(data: T? = null): ResponseWrapper<T> {
            return ResponseWrapper(networkState = NetworkState.LOADING, data = data)
        }

    }
}