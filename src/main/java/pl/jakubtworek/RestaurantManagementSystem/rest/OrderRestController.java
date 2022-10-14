package pl.jakubtworek.RestaurantManagementSystem.rest;

import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> getOrders(){
        return orderService.findAll();
    }

    @GetMapping("/order/id/{orderId}")
    public Order getOrderById(@PathVariable int orderId) throws Exception {
        if(orderService.findById(orderId) == null) throw new Exception("Order id not found - " + orderId);

        return orderService.findById(orderId);
    }

    @PostMapping("/{typeOfOrder}/order")
    public Order saveOrder(@PathVariable String typeOfOrder,@RequestBody Order theOrder){
        theOrder.setId(0);
        theOrder.setTypeOfOrder(orderService.findTypeByName(typeOfOrder));
        orderService.save(theOrder);

        return theOrder;
    }


    @DeleteMapping("/order/{orderId}")
    public String deleteOrder(@PathVariable int orderId) throws Exception {
        if(orderService.findById(orderId) == null) throw new Exception("Order id not found - " + orderId);
        orderService.deleteById(orderId);

        return "Deleted order is - " + orderId;
    }

    @GetMapping("/orders/date/{date}")
    public List<Order> getOrderByDate(@PathVariable String date) throws Exception {
        if(orderService.findByDate(date) == null) throw new Exception("Order id not found - " + date);

        return orderService.findByDate(date);
    }

    @GetMapping("/orders/typeOfOrder/{typeOfOrder}")
    public List<Order> getOrderByTypeofOrder(@PathVariable String typeOfOrder) throws Exception {
        if(orderService.findByTypeOfOrder(typeOfOrder) == null) throw new Exception("Order id not found - " + typeOfOrder);

        return orderService.findByTypeOfOrder(typeOfOrder);
    }

    @GetMapping("/orders/employee/{employeeId}")
    public List<Order> getOrderByEmployee(@PathVariable int employeeId) throws Exception {
        if(orderService.findByEmployee(employeeId) == null) throw new Exception("Order id not found - " + employeeId);

        return orderService.findByEmployee(employeeId);
    }

    @GetMapping("/orders/ready")
    public List<Order> getOrderMade() throws Exception {
        if(orderService.findMadeOrders() == null) throw new Exception("Order id not found");

        return orderService.findMadeOrders();
    }

    @GetMapping("/orders/unready")
    public List<Order> getOrderUnmade() throws Exception {
        if(orderService.findUnmadeOrders() == null) throw new Exception("Order id not found");

        return orderService.findUnmadeOrders();
    }
}
