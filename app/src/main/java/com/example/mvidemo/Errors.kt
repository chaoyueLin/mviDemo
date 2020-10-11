package com.example.mvidemo;

sealed class Errors : Throwable() {

    object NetworkError : Errors()

    object EmptyInputError : Errors()

    object EmptyResultsError : Errors()

    data class SimpleMessageError(val simpleMessage: String) : Errors()

    data class ErrorWrapper(val errors: Throwable) : Errors()
}