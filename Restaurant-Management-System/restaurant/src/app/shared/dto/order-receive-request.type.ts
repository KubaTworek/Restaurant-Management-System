export class OrderReceiveRequest {
  public orderId: string;
  public tip: string;

  constructor(orderId: string, tip: string) {
    this.orderId = orderId;
    this.tip = tip;
  }
}
