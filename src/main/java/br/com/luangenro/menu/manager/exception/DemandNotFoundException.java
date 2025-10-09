package br.com.luangenro.menu.manager.exception;

/**
 * Thrown to indicate that a requested demand (order) could not be found.
 *
 * <p>This is an unchecked exception used when a specific demand cannot be located by its
 * identifier, typically leading to a 404 Not Found API response.
 */
public class DemandNotFoundException extends RuntimeException {

  /**
   * Constructs a new DemandNotFoundException with the specified detail message.
   *
   * @param message the detail message, which is saved for later retrieval by the {@link
   *     #getMessage()} method.
   */
  public DemandNotFoundException(String message) {
    super(message);
  }
}
