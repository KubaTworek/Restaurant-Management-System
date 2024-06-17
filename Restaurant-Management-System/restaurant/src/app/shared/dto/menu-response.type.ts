export class MenuResponse {
  public id: string;
  public name: string;
  public menuItems: MenuItemResponse[];

  constructor(id: string, name: string, menuItems: MenuItemResponse[]) {
    this.id = id;
    this.name = name;
    this.menuItems = menuItems;
  }
}

export class MenuItemResponse {
  public id: string;
  public name: string;
  public price: string;
  public status: string;

  constructor(id: string, name: string, price: string, status: string) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.status = status;
  }
}

