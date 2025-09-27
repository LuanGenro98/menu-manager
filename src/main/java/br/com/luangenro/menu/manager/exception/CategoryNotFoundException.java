package br.com.luangenro.menu.manager.exception;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
