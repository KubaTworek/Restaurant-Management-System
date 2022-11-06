package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.GetOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderFactory {
    private final TypeOfOrderRepository typeOfOrderRepository;
    private final OrdersQueue ordersQueue;

    public OrderFactory(TypeOfOrderRepository typeOfOrderRepository, OrdersQueue ordersQueue) {
        this.typeOfOrderRepository = typeOfOrderRepository;
        this.ordersQueue = ordersQueue;
    }

    public Order createOrder(GetOrderDTO orderDTO){
        String typeOfOrder = orderDTO.getTypeOfOrder();
        switch(typeOfOrder){
            case "On-site":
                return createOnSiteOrder(orderDTO);
            case "Delivery":
                return createDeliveryOrder(orderDTO);
            default:
                return null;
        }
    }

    private Order createOnSiteOrder(GetOrderDTO orderDTO) {
        Order order = new Order();
        order.setId(0L);
        order.setTypeOfOrder(typeOfOrderRepository.findByType("On-site").get());
        order.setHourOrder(getTodayTime());
        order.setHourAway(null);
        order.setDate(getTodayDate());
        order.setPrice(countingOrderPrice(orderDTO));
        order.setMenuItems(orderDTO.convertDTOToEntity().getMenuItems());
        order.setEmployees(null);
        ordersQueue.add(order);

        return order;
    }

    private Order createDeliveryOrder(GetOrderDTO orderDTO) {
        Order order = new Order();
        order.setId(0L);
        order.setTypeOfOrder(typeOfOrderRepository.findByType("Delivery").get());
        order.setHourOrder(getTodayTime());
        order.setHourAway(null);
        order.setDate(getTodayDate());
        order.setPrice(countingOrderPrice(orderDTO));
        order.setMenuItems(orderDTO.convertDTOToEntity().getMenuItems());
        order.setEmployees(null);
        ordersQueue.add(order);

        return order;
    }

    private double countingOrderPrice(GetOrderDTO orderDTO){
        double price = 0;
        for(MenuItemDTO tempMenuItem : orderDTO.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }

    private String getTodayDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(localDateTime);
    }

    private String getTodayTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        return time.format(localDateTime);
    }
}
