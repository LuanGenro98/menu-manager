package br.com.luangenro.menu.manager.exception;

/**
 * Thrown to indicate that a requested item could not be found.
 *
 * <p>This is an unchecked exception used when a specific menu item cannot be located, typically
 * leading to a 404 Not Found API response.
 */
public class ItemNotFoundException extends RuntimeException {

  /**
   * Constructs a new ItemNotFoundException with the specified detail message.
   *
   * @param message the detail message, which is saved for later retrieval by the {@link
   *     #getMessage()} method.
   */
  public ItemNotFoundException(String message) {
    super(message);
  }
}
