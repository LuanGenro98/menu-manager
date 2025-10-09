package br.com.luangenro.menu.manager.domain.enumeration;

/**
 * Representa os possíveis status do ciclo de vida de um Pedido (Demand) dentro do sistema.
 *
 * <p>Este enum é usado para rastrear o progresso de um pedido desde sua criação até sua finalização
 * ou cancelamento, permitindo que tanto o estabelecimento quanto o cliente acompanhem seu
 * andamento.
 */
public enum DemandStatus {

  /**
   * O pedido foi criado e recebido com sucesso pelo sistema, mas ainda aguarda para ser iniciado
   * pela cozinha. Este é o estado inicial de todo novo pedido.
   */
  RECEBIDO,

  /** O pedido foi aceito e a cozinha iniciou o seu preparo. */
  EM_PREPARO,

  /**
   * O pedido está finalizado na cozinha e pronto para ser retirado pelo garçom e servido à mesa.
   */
  PRONTO,

  /**
   * O pedido foi fisicamente entregue ao cliente na mesa. O próximo passo geralmente é o pagamento.
   */
  ENTREGUE,

  /**
   * O pedido foi pago pelo cliente. Este é um estado final para um fluxo de pedido bem-sucedido.
   */
  PAGO,

  /**
   * O pedido foi cancelado, seja a pedido do cliente ou por uma decisão do estabelecimento (ex:
   * falta de um item). Este é um estado final e interrompe o fluxo normal.
   */
  CANCELADO
}
