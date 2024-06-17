export class OrderResponse {
  public id: string;
  public price: OrderPriceResponse;
  public hourOrder: string;
  public hourPrepared: string;
  public hourReceived: string;
  public typeOfOrder: string;
  public status: string;
  public delivery: OrderDeliveryResponse;
  public orderItems: OrderItemResponse[];

  constructor(id: string, price: OrderPriceResponse, hourOrder: string, hourPrepared: string, hourReceived: string, typeOfOrder: string, status: string, delivery: OrderDeliveryResponse, orderItems: OrderItemResponse[]) {
    this.id = id;
    this.price = price;
    this.hourOrder = hourOrder;
    this.hourPrepared = hourPrepared;
    this.hourReceived = hourReceived;
    this.typeOfOrder = typeOfOrder;
    this.status = status;
    this.delivery = delivery;
    this.orderItems = orderItems;
  }
}

export class OrderPriceResponse {
  public id: string;
  public price: string;
  public deliveryFee: string;
  public minimumBasketFee: string;
  public tip: string;

  constructor(id: string, price: string, deliveryFee: string, minimumBasketFee: string, tip: string) {
    this.id = id;
    this.price = price;
    this.deliveryFee = deliveryFee;
    this.minimumBasketFee = minimumBasketFee;
    this.tip = tip;
  }
}

export class OrderDeliveryResponse {
  public id: string;
  public hourStart: string;
  public hourEnd: string;
  public status: string;
  public district: string;
  public street: string;
  public houseNumber: string;

  constructor(id: string, hourStart: string, hourEnd: string, status: string, district: string, street: string, houseNumber: string) {
    this.id = id;
    this.hourStart = hourStart;
    this.hourEnd = hourEnd;
    this.status = status;
    this.district = district;
    this.street = street;
    this.houseNumber = houseNumber;
  }
}

export class OrderItemResponse {
  public id: string;
  public name: string;
  public price: string;
  public amount: string;

  constructor(id: string, name: string, price: string, amount: string) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.amount = amount;
  }
}


