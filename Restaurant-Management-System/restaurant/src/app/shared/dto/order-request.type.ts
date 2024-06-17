export class OrderRequest {
  public typeOfOrder: string;
  public menuItems: string[];
  public address: AddressRequest | null;

  constructor(typeOfOrder: string, menuItems: string[], address: AddressRequest | null) {
    this.typeOfOrder = typeOfOrder;
    this.menuItems = menuItems;
    this.address = address;
  }
}

export class AddressRequest {
  public district: string;
  public street: string;
  public houseNumber: string;

  constructor(district: string, street: string, houseNumber: string) {
    this.district = district;
    this.street = street;
    this.houseNumber = houseNumber;
  }
}
