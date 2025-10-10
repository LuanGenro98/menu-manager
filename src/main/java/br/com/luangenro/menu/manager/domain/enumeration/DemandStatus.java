package br.com.luangenro.menu.manager.domain.enumeration;

/**
 * Represents the possible lifecycle statuses of a Demand (order) within the system.
 *
 * <p>This enum is used to track the progress of an order from its creation to its completion or
 * cancellation, allowing both the establishment and the customer to follow its progress.
 */
public enum DemandStatus {

  /**
   * The order has been created and received by the system, but is still waiting to be prepared.
   * This is the initial state for every new order.
   */
  ORDERED,

  /** The order has been accepted and the kitchen has started its preparation. */
  PREPARING,

  /**
   * The order is finished in the kitchen and ready to be picked up by the waiter and served to the
   * table.
   */
  READY,

  /**
   * The order has been physically delivered to the customer at the table. The next step is usually
   * payment.
   */
  SERVED,

  /** The order has been paid by the customer. This is a final state for a successful order flow. */
  PAID,

  /**
   * The order has been canceled, either at the customer's request or by a decision of the
   * establishment (e.g., an item is out of stock). This is a final state and interrupts the normal
   * flow.
   */
  CANCELED
}
