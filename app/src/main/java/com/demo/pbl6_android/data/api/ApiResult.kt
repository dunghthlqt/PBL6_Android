package com.demo.pbl6_android.data.api

/**
 * A generic sealed class to represent the result of an API call
 * This helps handle different states: Success, Error, and Loading
 */
sealed class ApiResult<out T> {
    
    /**
     * Represents a successful API response
     * @param data The data returned from the API
     */
    data class Success<T>(val data: T) : ApiResult<T>()
    
    /**
     * Represents an error during API call
     * @param message Error message to display to user
     * @param exception The underlying exception (optional)
     * @param code HTTP status code (optional)
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null,
        val code: Int? = null
    ) : ApiResult<Nothing>()
    
    /**
     * Represents a loading state during API call
     */
    data object Loading : ApiResult<Nothing>()
    
    /**
     * Check if the result is successful
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Check if the result is an error
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Check if the result is loading
     */
    fun isLoading(): Boolean = this is Loading
    
    /**
     * Get data if success, null otherwise
     */
    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Get error message if error, null otherwise
     */
    fun getErrorMessageOrNull(): String? = when (this) {
        is Error -> message
        else -> null
    }
}

/**
 * Extension function to convert ApiResult to another type
 */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> ApiResult.Error(message, exception, code)
        is ApiResult.Loading -> ApiResult.Loading
    }
}

/**
 * Extension function to handle success case
 */
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(data)
    }
    return this
}

/**
 * Extension function to handle error case
 */
inline fun <T> ApiResult<T>.onError(action: (String, Throwable?, Int?) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        action(message, exception, code)
    }
    return this
}

/**
 * Extension function to handle loading case
 */
inline fun <T> ApiResult<T>.onLoading(action: () -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) {
        action()
    }
    return this
}

