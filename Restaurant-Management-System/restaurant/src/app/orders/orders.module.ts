import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { OrdersCreateComponent } from './orders-create/orders-create.component';
import { OrdersOngoingComponent } from './orders-ongoing/orders-ongoing.component';
import { OrdersComponent } from './orders.component';
import { OrdersAllComponent } from './orders-all/orders-all.component';
import { OrdersRoutingModule } from './orders-routing.module';

@NgModule({
  declarations: [
    OrdersComponent,
    OrdersOngoingComponent,
    OrdersCreateComponent,
    OrdersAllComponent,
  ],
  imports: [
    SharedModule,
    OrdersRoutingModule,
    TranslateModule,
  ],
})
export class OrdersModule {}
