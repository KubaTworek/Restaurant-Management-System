import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: "orders",
    loadChildren: () =>
      import("./orders/orders.module").then(
        m => m.OrdersModule
      )
  },
  {
    path: "auth",
    loadChildren: () =>
      import("./auth/auth.module").then(
        m => m.AuthModule
      )
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
