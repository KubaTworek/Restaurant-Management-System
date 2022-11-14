package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.RequiredArgsConstructor;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<Order> ordersFound = orderService.findAll();
        List<OrderResponse> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<OrderResponse> getOrderById(@RequestParam Long id) throws OrderNotFoundException {
        if(orderService.checkIfOrderIsNull(id)){
            throw new OrderNotFoundException("There are no order in restaurant with that id: " + id);
        }
        Order orderFound = orderService.findById(id).get();
        OrderResponse orderResponse = orderFound.convertEntityToResponse();
        addLinkToDTO(orderResponse);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody OrderRequest dto) throws TypeOfOrderNotFoundException {
        if(typeOfOrderService.checkIfTypeOfOrderIsNull(dto.getTypeOfOrder())){
            throw new TypeOfOrderNotFoundException("There are no that type of order in restaurant with name: " + dto.getTypeOfOrder());
        }
        Order orderSaved = orderService.save(dto);
        OrderResponse orderResponse = orderSaved.convertEntityToResponse();
        addLinkToDTO(orderResponse);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/id")
    public ResponseEntity<String> deleteOrder(@RequestParam Long id) throws OrderNotFoundException {
        if(orderService.checkIfOrderIsNull(id)){
            throw new OrderNotFoundException("Order id not found - " + id);
        }
        orderService.deleteById(id);

        return new ResponseEntity<>("Deleted order has id: " + id, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<OrderResponse>> getOrderByParams(@RequestParam(required = false) String date,
                                                                @RequestParam(required = false) String typeOfOrder,
                                                                @RequestParam(required = false) Long employeeId
                                                         ) throws TypeOfOrderNotFoundException, EmployeeNotFoundException {
        List<Order> ordersFound1 = new ArrayList<>();
        List<Order> ordersFound2 = new ArrayList<>();
        List<Order> ordersFound3 = new ArrayList<>();

        if(date != null){
            ordersFound1 = orderService.findByDate(date);
        }
        if(typeOfOrder != null){
            if(typeOfOrderService.checkIfTypeOfOrderIsNull(typeOfOrder)) {
                throw new TypeOfOrderNotFoundException("There are no type of orders like that in restaurant with that name: " + typeOfOrder);
            }
            TypeOfOrder typeOfOrderFound = typeOfOrderService.findByType(typeOfOrder).get();
            ordersFound2 = orderService.findByTypeOfOrder(typeOfOrderFound);
        }
        if(employeeId != null){
            if(employeeService.checkIfEmployeeIsNull(employeeId)) {
                throw new EmployeeNotFoundException("There are no employees  with that id: " + employeeId);
            }
            Employee employeeFound = employeeService.findById(employeeId).get();
            ordersFound3 = orderService.findByEmployee(employeeFound);
        }

        Set<Order> ordersFound = new HashSet<>();
        ordersFound.addAll(ordersFound1);
        ordersFound.addAll(ordersFound2);
        ordersFound.addAll(ordersFound3);

        List<OrderResponse> ordersDTO = createDTOList(ordersFound);
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderResponse>> getOrderMade() {
        List<Order> ordersFound = orderService.findMadeOrders();
        List<OrderResponse> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/unready")
    public ResponseEntity<List<OrderResponse>> getOrderUnmade() {
        List<Order> ordersFound = orderService.findUnmadeOrders();
        List<OrderResponse> ordersDTO = createDTOList(ordersFound);

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    private List<OrderResponse> createDTOList(List<Order> orderEntities){
        List<OrderResponse> ordersDTO = orderEntities
                .stream()
                .map(Order::convertEntityToResponse)
                .collect(Collectors.toList());

        ordersDTO.forEach(this::addLinkToDTO);

        return ordersDTO;
    }

    private List<OrderResponse> createDTOList(Set<Order> orderEntities){
        List<OrderResponse> ordersDTO = orderEntities
                .stream()
                .map(Order::convertEntityToResponse)
                .collect(Collectors.toList());

        ordersDTO.forEach(this::addLinkToDTO);

        return ordersDTO;
    }

    private void addLinkToDTO(OrderResponse dto){
        dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash(dto.getId()).withSelfRel());
        for(EmployeeResponse e: dto.getEmployees()){
            e.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(e.getId()).withSelfRel());
        }
    }
}