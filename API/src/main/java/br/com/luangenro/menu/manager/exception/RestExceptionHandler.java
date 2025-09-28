package br.com.luangenro.menu.manager.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the REST API.
 *
 * <p>This class uses @ControllerAdvice to act as a centralized point for handling exceptions across
 * all controllers, ensuring consistent and well-structured error responses.
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  /**
   * Handles exceptions for resources that could not be found. Catches the custom
   * CategoryNotFoundException.
   *
   * @param ex The exception that was thrown.
   * @param request The current web request.
   * @return A ResponseEntity with a 404 Not Found status and a standardized error body.
   */
  @ExceptionHandler({CategoryNotFoundException.class, ItemNotFoundException.class})
  public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
      RuntimeException ex, HttpServletRequest request) {

    log.warn("Resource not found: {}", ex.getMessage());
    ApiErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles validation exceptions triggered by @Valid on request bodies. Extracts field-specific
   * errors and formats them in the 'details' part of the response.
   *
   * @param ex The MethodArgumentNotValidException that was thrown.
   * @param request The current web request.
   * @return A ResponseEntity with a 400 Bad Request status and a detailed error body.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<Map<String, String>> errorDetails =
        ex.getBindingResult().getAllErrors().stream()
            .map(
                error -> {
                  String fieldName =
                      (error instanceof FieldError)
                          ? ((FieldError) error).getField()
                          : error.getObjectName();
                  String errorMessage = error.getDefaultMessage();
                  return Map.of("field", fieldName, "message", errorMessage);
                })
            .collect(Collectors.toList());

    log.warn("Validation failure: {}", errorDetails);
    String message = "Validation failed. Check the 'details' for more information.";
    ApiErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, errorDetails);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles database integrity constraint violations. This is typically thrown for foreign key
   * violations or unique constraint failures.
   *
   * @param ex The DataIntegrityViolationException that was thrown.
   * @param request The current web request.
   * @return A ResponseEntity with a 400 Bad Request status, indicating invalid client data.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex, HttpServletRequest request) {

    log.error("Database integrity violation: {}", ex.getMostSpecificCause().getMessage());
    String message =
        "Database error: A related entity does not exist or a data constraint was violated.";
    ApiErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * A catch-all handler for any other unhandled exceptions. This prevents stack traces from being
   * exposed to the client and provides a generic server error response.
   *
   * @param ex The exception that was thrown.
   * @param request The current web request.
   * @return A ResponseEntity with a 500 Internal Server Error status.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleAllUncaughtException(
      Exception ex, HttpServletRequest request) {

    log.error("An unexpected error occurred", ex);
    String message = "An unexpected internal server error has occurred.";
    ApiErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles exceptions when the request body is malformed or unreadable. This is typically thrown
   * when the client sends an invalid JSON payload.
   *
   * @param ex The HttpMessageNotReadableException that was thrown.
   * @param request The current web request.
   * @return A ResponseEntity with a 400 Bad Request status.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpServletRequest request) {

    log.warn("Failed to parse request body: {}", ex.getMessage());
    String message = "Failed to parse request body. Please ensure it is a valid JSON format.";
    ApiErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Factory method to build a standardized {@link ApiErrorResponse}.
   *
   * @param status The HTTP status for the response.
   * @param message The main error message.
   * @param request The current web request to extract the path from.
   * @param details (Optional) A list of specific details about the error.
   * @return A fully constructed {@code ApiErrorResponse} object.
   */
  private ApiErrorResponse buildErrorResponse(
      HttpStatus status,
      String message,
      HttpServletRequest request,
      List<Map<String, String>> details) {
    return new ApiErrorResponse(
        LocalDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI(),
        details);
  }
}
