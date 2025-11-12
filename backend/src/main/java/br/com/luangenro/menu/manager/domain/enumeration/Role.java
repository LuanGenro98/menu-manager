package br.com.luangenro.menu.manager.domain.enumeration;

/**
 * Defines the different access profiles (roles) that a user can have in the system.
 *
 * <p>These roles are used by Spring Security to control authorization for specific endpoints,
 * ensuring that only users with the correct permissions can perform certain actions.
 */
public enum Role {

  /**
   * Standard user profile, typically associated with customers.
   *
   * <p>Users with this role can view the menu and create their own demands (orders), but do not
   * have access to administrative functionalities such as modifying the menu or managing other
   * orders.
   */
  USER,

  /**
   * Administrator profile with full and unrestricted access to the system.
   *
   * <p>Administrators can perform all the actions of a standard user, in addition to managing the
   * menu (CRUD for items and categories), managing all demands (updating status, deleting), and
   * potentially managing other users.
   */
  ADMIN
}
