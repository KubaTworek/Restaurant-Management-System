export class OrderConfirmRequest {
  public orderId: string;
  public decision: string;

  constructor(orderId: string, decision: string) {
    this.orderId = orderId;
    this.decision = decision;
  }
}
