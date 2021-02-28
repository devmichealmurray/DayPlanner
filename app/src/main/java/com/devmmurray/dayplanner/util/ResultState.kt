package com.devmmurray.dayplanner.util

data class ResultState<out T>(val status: Status, val data: T?, val error: Error?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ResultState<T> {
            return ResultState(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: Error?): ResultState<T> {
            return ResultState(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): ResultState<T> {
            return ResultState(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}