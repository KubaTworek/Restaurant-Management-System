package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.TypeOfOrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final EmployeeService employeeService;

    private final OrdersQueue ordersQueue;

    @Autowired
    public OrderController(OrderService orderService, TypeOfOrderService typeOfOrderService, EmployeeService employeeService, OrdersQueue ordersQueue) {
        this.orderService = orderService;
        this.typeOfOrderService = typeOfOrderService;
        this.employeeService = employeeService;
        this.ordersQueue = ordersQueue;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderService.findAll();
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) throws OrderNotFoundException {
        if(orderService.checkIfOrderIsNull(orderId)){
            throw new OrderNotFoundException("There are no order in restaurant with that id: " + orderId);
        }
        Order orderFound = orderService.findById(orderId).get();
        OrderDTO dto = orderFound.convertEntityToDTO();
        addLinkToDTO(dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO dto) {

        dto.setId(0L);
        dto.setPrice(countingOrderPrice(dto));

        setDataForDTO(dto);

        Order order = orderService.save(dto.convertDTOToEntity());
        ordersQueue.add(order);

        order.setTypeOfOrder(dto.getTypeOfOrder().convertDTOToEntity());
        order.getTypeOfOrder().add(order);

        OrderDTO orderDTO = order.convertEntityToDTO();
        addLinkToDTO(orderDTO);

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) throws OrderNotFoundException {
        if(orderService.checkIfOrderIsNull(orderId)){
            throw new OrderNotFoundException("Order id not found - " + orderId);
        }
        orderService.deleteById(orderId);

        return new ResponseEntity<>("Deleted order has id: " + orderId, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<OrderDTO>> getOrderByDate(@PathVariable String date) {

        List<Order> orders = orderService.findByDate(date);
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/type-of-order/{typeOfOrderName}")
    public ResponseEntity<List<OrderDTO>> getOrderByTypeOfOrder(@PathVariable String typeOfOrderName) throws TypeOfOrderNotFoundException {
        if(typeOfOrderService.checkIfTypeOfOrderIsNull(typeOfOrderName)) {
            throw new TypeOfOrderNotFoundException("There are no type of orders like that in restaurant with that name: " + typeOfOrderName);
        }
        TypeOfOrder typeOfOrder = typeOfOrderService.findByType(typeOfOrderName).get();
        List<Order> orders = orderService.findByTypeOfOrder(typeOfOrder);
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<OrderDTO>> getOrderByEmployee(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(employeeId)) {
            throw new EmployeeNotFoundException("There are no temployees  with that id: " + employeeId);
        }
        Employee employee = employeeService.findById(employeeId).get();
        List<Order> orders = orderService.findByEmployee(employee);
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderDTO>> getOrderMade() throws Exception {
        List<Order> orders = orderService.findMadeOrders();
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/unready")
    public ResponseEntity<List<OrderDTO>> getOrderUnmade() {
        List<Order> orders = orderService.findUnmadeOrders();
        List<OrderDTO> orderDTOS = createDTOList(orders);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    private double countingOrderPrice(OrderDTO theOrder){
        double price = 0;
        for(MenuItemDTO tempMenuItem : theOrder.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }

    private void setDataForDTO(OrderDTO dto){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        dto.setDate(date.format(localDateTime));
        dto.setHourOrder(time.format(localDateTime));
    }

    private List<OrderDTO> createDTOList(List<Order> orderEntities){
        List<OrderDTO> ordersDTO = orderEntities
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        ordersDTO.forEach(this::addLinkToDTO);

        return ordersDTO;
    }

    private void addLinkToDTO(OrderDTO dto){
        dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash(dto.getId()).withSelfRel());
        for(EmployeeDTO e: dto.getEmployees()){
            e.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(e.getId()).withSelfRel());
        }
    }
}