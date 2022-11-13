package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.TypeOfOrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final EmployeeService employeeService;

    @Autowired
    public OrderController(OrderService orderService, TypeOfOrderService typeOfOrderService, EmployeeService employeeService) {
        this.orderService = orderService;
        this.typeOfOrderService = typeOfOrderService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> ordersFound = orderService.findAll();
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) throws OrderNotFoundException {
        if(orderService.checkIfOrderIsNull(orderId)){
            throw new OrderNotFoundException("There are no order in restaurant with that id: " + orderId);
        }
        Order orderFound = orderService.findById(orderId).get();
        OrderDTO orderDTO = orderFound.convertEntityToDTO();
        addLinkToDTO(orderDTO);

        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody GetOrderDTO dto) throws TypeOfOrderNotFoundException {
        if(typeOfOrderService.checkIfTypeOfOrderIsNull(dto.getTypeOfOrder())){
            throw new TypeOfOrderNotFoundException("There are no that type of order in restaurant with name: " + dto.getTypeOfOrder());
        }
        Order orderSaved = orderService.save(dto);
        OrderDTO orderDTO = orderSaved.convertEntityToDTO();
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
        List<Order> ordersFound = orderService.findByDate(date);
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/type-of-order/{typeOfOrderName}")
    public ResponseEntity<List<OrderDTO>> getOrderByTypeOfOrder(@PathVariable String typeOfOrderName) throws TypeOfOrderNotFoundException {
        if(typeOfOrderService.checkIfTypeOfOrderIsNull(typeOfOrderName)) {
            throw new TypeOfOrderNotFoundException("There are no type of orders like that in restaurant with that name: " + typeOfOrderName);
        }
        TypeOfOrder typeOfOrderFound = typeOfOrderService.findByType(typeOfOrderName).get();
        List<Order> ordersFound = orderService.findByTypeOfOrder(typeOfOrderFound);
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<OrderDTO>> getOrderByEmployee(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(employeeId)) {
            throw new EmployeeNotFoundException("There are no temployees  with that id: " + employeeId);
        }
        Employee employeeFound = employeeService.findById(employeeId).get();
        List<Order> ordersFound = orderService.findByEmployee(employeeFound);
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderDTO>> getOrderMade() {
        List<Order> ordersFound = orderService.findMadeOrders();
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/unready")
    public ResponseEntity<List<OrderDTO>> getOrderUnmade() {
        List<Order> ordersFound = orderService.findUnmadeOrders();
        List<OrderDTO> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
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
        for(EmployeeResponse e: dto.getEmployees()){
            e.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(e.getId()).withSelfRel());
        }
    }
}