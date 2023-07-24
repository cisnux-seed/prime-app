package dev.cisnux.core.utils

import io.ktor.http.HttpStatusCode

sealed class Failure(override var message: String?) : Exception(message) {
    data class ConnectionFailure(
        override var message: String? = "😞No internet connection",
    ) : Failure(message)

    data class NotFoundFailure(
        override var message: String? = null,
    ) : Failure(message)

    data class ServerFailure(
        override var message: String? = null,
    ) : Failure(message)

    class BadRequestFailure(
        override var message: String? = null,
    ) : Failure(message)

    class UnauthorizedFailure(
        override var message: String? = null,
    ) : Failure(message)

    companion object {
        val MOVIES_HTTP_FAILURES = mapOf(
            HttpStatusCode.BadRequest to BadRequestFailure(),
            HttpStatusCode.Unauthorized to UnauthorizedFailure(),
            HttpStatusCode.NotFound to NotFoundFailure(message = "the movie is not available"),
            HttpStatusCode.InternalServerError to ServerFailure()
        )
        val KEYWORD_HTTP_FAILURES = mapOf(
            HttpStatusCode.BadRequest to BadRequestFailure(),
            HttpStatusCode.Unauthorized to UnauthorizedFailure(),
            HttpStatusCode.InternalServerError to ServerFailure()
        )
    }
}


