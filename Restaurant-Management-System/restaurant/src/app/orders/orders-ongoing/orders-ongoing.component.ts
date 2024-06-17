import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { DataStorageService } from "../../shared/data-storage.service";
import { OrderResponse } from "../../shared/dto/order-response.type";
import { OrderReceiveRequest } from "../../shared/dto/order-receive-request.type";

@Component({
  selector: 'orders-ongoing',
  templateUrl: './orders-ongoing.component.html',
  styleUrls: ['./orders-ongoing.component.scss'],
})
export class OrdersOngoingComponent implements OnInit, OnDestroy {
  orders: OrderResponse[] = [];
  showSummaryModal = false;
  showSuccessModal = false;
  selectedOrder: OrderResponse | null = null;
  tip = "0.00";

  private ordersSubscription = new Subscription();

  constructor(private dataStorageService: DataStorageService) {}

  ngOnInit(): void {
    this.ordersSubscription = this.dataStorageService.fetchOngoingOrders().subscribe(
      (orders: OrderResponse[]) => {
        this.orders = orders;
      }
    );
  }

  ngOnDestroy(): void {
    this.ordersSubscription.unsubscribe();
  }

  openSummaryModal(order: OrderResponse): void {
    this.selectedOrder = order;
    this.tip = "0.00";
    this.showSummaryModal = true;
  }

  closeSummaryModal(): void {
    this.showSummaryModal = false;
  }

  openSuccessModal(): void {
    this.showSuccessModal = true;
  }

  closeSuccessModal(): void {
    location.reload();
    this.showSuccessModal = false;
  }

  receiveOrder(): void {
    if (this.selectedOrder) {
      const orderReceiveRequest = new OrderReceiveRequest(this.selectedOrder.id, this.tip);

      this.dataStorageService.receiveOrder(orderReceiveRequest);
      this.closeSummaryModal();
      this.openSuccessModal();
    }
  }

  calculateTotalPrice(order: OrderResponse, tip: string): number {
    const productPrice = Number(order.price.price);
    const deliveryFee = Number(order.price.deliveryFee);
    const minimumBasketFee = Number(order.price.minimumBasketFee);
    const tipAmount = Number(tip);
    return productPrice + deliveryFee + minimumBasketFee + tipAmount;
  }

  formatPrice(price: string | number): string {
    return Number(price).toFixed(2);
  }
}
