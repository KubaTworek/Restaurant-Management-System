import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrdersComponent } from './orders.component';
import { OrdersOngoingComponent } from './orders-ongoing/orders-ongoing.component';
import { OrdersAllComponent } from './orders-all/orders-all.component';
import { OrdersCreateComponent } from './orders-create/orders-create.component';

const routes: Routes = [
  {
    path: '',
    component: OrdersComponent,
    children: [
      { path: '', redirectTo: 'all', pathMatch: 'full' },
      { path: 'ongoing', component: OrdersOngoingComponent },
      { path: 'all', component: OrdersAllComponent },
    ],
  },
  { path: 'create', component: OrdersCreateComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OrdersRoutingModule {}
