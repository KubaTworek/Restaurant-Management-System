import { Component, OnInit } from "@angular/core";
import { DataStorageService } from "../../shared/data-storage.service";
import { OrderResponse } from "../../shared/dto/order-response.type";

@Component({
  selector: 'orders-all',
  templateUrl: './orders-all.component.html',
  styleUrls: ['./orders-all.component.scss'],
})
export class OrdersAllComponent implements OnInit {
  orders: OrderResponse[] = [];
  selectedOrder: OrderResponse | null = null;
  showOrderDetailsModal = false;
  tip = "0.00";

  districts = [
    { name: 'SRODMIESCIE', value: 'Śródmieście' },
    { name: 'ZOLIBORZ', value: 'Żoliborz' },
    { name: 'WOLA', value: 'Wola' },
    { name: 'OCHOTA', value: 'Ochota' },
    { name: 'MOKOTOW', value: 'Mokotów' },
    { name: 'PRAGA', value: 'Praga' },
    { name: 'TARGOWEK', value: 'Targówek' },
    { name: 'REMBERTOW', value: 'Rembertów' },
    { name: 'WAWER', value: 'Wawer' },
    { name: 'WESOLA', value: 'Wesoła' },
    { name: 'WILANOW', value: 'Wilanów' },
    { name: 'URSYNOW', value: 'Ursynów' },
    { name: 'WLOCHY', value: 'Włochy' },
    { name: 'URSUS', value: 'Ursus' },
    { name: 'BEMOWO', value: 'Bemowo' },
    { name: 'BIELAY', value: 'Bielany' },
    { name: 'BIALOLEKA', value: 'Białołęka' }
  ];

  constructor(private dataStorageService: DataStorageService) {}

  ngOnInit(): void {
    this.fetchOrders();
  }

  fetchOrders(): void {
    this.dataStorageService.fetchAllOrders().subscribe((orders: OrderResponse[]) => {
      this.orders = orders;
    });
  }

  getDistrictValue(districtName: string): string {
    const district = this.districts.find(d => d.name === districtName);
    return district ? district.value : 'Unknown';
  }

  calculateTotalPrice(order: OrderResponse): number {
    const productPrice = Number(order.price.price);
    const deliveryFee = Number(order.price.deliveryFee);
    const minimumBasketFee = Number(order.price.minimumBasketFee);
    const tipAmount = Number(order.price.tip);
    return productPrice + deliveryFee + minimumBasketFee + tipAmount;
  }

  showOrderDetails(order: OrderResponse): void {
    this.dataStorageService.fetchOrder(order.id).subscribe((order: OrderResponse) => {
      this.selectedOrder = order;
      this.showOrderDetailsModal = true;
    });
  }

  closeOrderDetailsModal(): void {
    this.showOrderDetailsModal = false;
    this.selectedOrder = null;
  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }
}
