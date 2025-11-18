package br.com.luangenro.menu.manager.exception;

/**
 * Thrown to indicate that a requested category could not be found.
 *
 * <p>This is an unchecked exception, as it typically represents a client error (e.g., requesting a
 * resource with a non-existent ID), which should result in a 404 Not Found response.
 */
public class CategoryNotFoundException extends RuntimeException {

  /**
   * Constructs a new CategoryNotFoundException with the specified detail message.
   *
   * @param message the detail message, which is saved for later retrieval by the {@link
   *     #getMessage()} method.
   */
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
