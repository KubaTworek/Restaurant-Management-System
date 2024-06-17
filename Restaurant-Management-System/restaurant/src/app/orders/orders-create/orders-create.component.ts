import { Component, OnInit } from '@angular/core';
import { MenuItemResponse, MenuResponse } from "../../shared/dto/menu-response.type";
import { DataStorageService } from "../../shared/data-storage.service";
import { AddressRequest, OrderRequest } from "../../shared/dto/order-request.type";
import { OrderResponse } from "../../shared/dto/order-response.type";
import {OrderConfirmRequest} from "../../shared/dto/order-confirm-request.tye";

@Component({
  selector: 'orders-create',
  templateUrl: './orders-create.component.html',
  styleUrls: ['./orders-create.component.scss'],
})
export class OrdersCreateComponent implements OnInit {
  menus: MenuResponse[] = [];
  selectedCategory: MenuResponse | null = null;
  orderedItemsMap: Map<string, { item: MenuItemResponse, quantity: number }> = new Map();
  showAddressModal = false;
  showSummaryModal = false;
  address: AddressRequest = new AddressRequest('', '', '');
  deliveryChecked = false;
  orderResponse: OrderResponse | null = null;
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
    this.dataStorageService.fetchMenus().subscribe((menus: MenuResponse[]) => {
      this.menus = menus;
      this.selectedCategory = menus.length > 0 ? menus[0] : null;
    });
  }

  selectCategory(category: MenuResponse): void {
    this.selectedCategory = category;
  }

  addItemToOrder(item: MenuItemResponse): void {
    if (this.orderedItemsMap.has(item.id)) {
      this.orderedItemsMap.get(item.id)!.quantity++;
    } else {
      this.orderedItemsMap.set(item.id, { item, quantity: 1 });
    }
  }

  removeItemFromOrder(item: MenuItemResponse): void {
    if (this.orderedItemsMap.has(item.id)) {
      const orderItem = this.orderedItemsMap.get(item.id)!;
      if (orderItem.quantity > 1) {
        orderItem.quantity--;
      } else {
        this.orderedItemsMap.delete(item.id);
      }
    }
  }

  getOrderedItems(): { item: MenuItemResponse, quantity: number }[] {
    return Array.from(this.orderedItemsMap.values());
  }

  getTotalPriceForItem(item: MenuItemResponse, quantity: number): number {
    return Number(item.price) * quantity;
  }

  openAddressModal(): void {
    this.showAddressModal = true;
  }

  closeAddressModal(): void {
    this.showAddressModal = false;
  }

  openSummaryModal(): void {
    this.showSummaryModal = true;
  }

  closeSummaryModal(): void {
    this.resetOrder();
    this.showSummaryModal = false;
  }

  submitAddress(event: Event): void {
    event.preventDefault();
    this.createOrder();
    this.closeAddressModal();
    this.openSummaryModal();
  }

  startOrder(): void {
    if (this.getOrderedItems().length === 0) {
      return; // Do nothing if no items are ordered
    }

    if (this.deliveryChecked) {
      this.openAddressModal();
    } else {
      this.createOrder();
      this.openSummaryModal();
    }
  }

  createOrder(): void {
    const menuItems: string[] = [];
    this.getOrderedItems().forEach(orderedItem => {
      for (let i = 0; i < orderedItem.quantity; i++) {
        menuItems.push(orderedItem.item.name);
      }
    });
    const address = this.deliveryChecked ? this.address : null;
    const typeOfOrder = this.deliveryChecked ? "DELIVERY" : "ON_SITE";
    const orderRequest = new OrderRequest(typeOfOrder, menuItems, address);
    this.dataStorageService.createOrder(orderRequest).subscribe((response: OrderResponse) => {
      this.orderResponse = response;
      this.openSummaryModal();
    });
  }

  acceptOrder(): void {
    this.dataStorageService.confirmOrder(new OrderConfirmRequest(this.orderResponse!.id, "ACCEPT"));
    this.closeSummaryModal();
  }

  cancelOrder(): void {
    this.dataStorageService.confirmOrder(new OrderConfirmRequest(this.orderResponse!.id, "REJECT"));
    this.closeSummaryModal();
  }

  resetOrder(): void {
    this.orderedItemsMap.clear();
    this.deliveryChecked = false;
    this.address = new AddressRequest('', '', '');
    this.selectedCategory = this.menus.length > 0 ? this.menus[0] : null;
  }

  calculateTotalPrice(): number {
    return Number(this.orderResponse?.price.price) +
      Number(this.orderResponse?.price.minimumBasketFee) +
      Number(this.orderResponse?.price.deliveryFee);
  }

  formatPrice(price: any): string {
    return price.toFixed(2);
  }
}
