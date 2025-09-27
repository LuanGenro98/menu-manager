package br.com.luangenro.menu.manager.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a standardized error response for the API. This structure is used for all client-side
 * and server-side error responses.
 *
 * @param timestamp The exact time the error occurred.
 * @param status The HTTP status code.
 * @param error A short, human-readable representation of the status code (e.g., "Not Found").
 * @param message A clear, descriptive message explaining the error.
 * @param path The API path that was requested.
 * @param details (Optional) A list of specific error details, typically used for validation
 *     failures.
 */
@Schema(description = "Standardized structure for API error responses.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
    @Schema(description = "Timestamp of when the error occurred.", example = "2025-09-27T16:07:10")
        LocalDateTime timestamp,
    @Schema(description = "The HTTP status code.", example = "404") int status,
    @Schema(description = "The HTTP status error reason phrase.", example = "Not Found")
        String error,
    @Schema(
            description = "A detailed message explaining the error.",
            example = "The requested category with ID 10 does not exist.")
        String message,
    @Schema(
            description = "The path of the request that resulted in an error.",
            example = "/api/v1/categories/10")
        String path,
    @Schema(description = "A list of specific validation errors or further details.")
        List<Map<String, String>> details) {}
