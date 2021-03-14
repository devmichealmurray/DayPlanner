package com.devmmurray.dayplanner.util

data class ResultStatus<out T>(val status: Status, val data: T?, val error: Error?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ResultStatus<T> {
            return ResultStatus(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: Error?): ResultStatus<T> {
            return ResultStatus(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): ResultStatus<T> {
            return ResultStatus(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}