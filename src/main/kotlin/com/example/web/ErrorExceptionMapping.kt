package com.example.web

internal data class ErrorResponse(val errors: Map<String, List<String?>>)

class ErrorExceptionMapping {
}