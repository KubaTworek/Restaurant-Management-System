package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.TypeOfOrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderResponse> ordersFound = orderService.findAll()
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<OrderResponse> getOrderById(@RequestParam Long id) throws OrderNotFoundException {

        OrderResponse orderResponse = orderService.findById(id)
                .map(OrderDTO::convertDTOToResponse)
                .orElseThrow(() -> new OrderNotFoundException("There are no order in restaurant with that id: " + id));

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody OrderRequest orderRequest) throws TypeOfOrderNotFoundException {

        TypeOfOrderDTO typeOfOrderDTO = typeOfOrderService.findByType(orderRequest.getTypeOfOrder())
                .orElseThrow(TypeOfOrderNotFoundException::new);
        OrderResponse orderResponse = orderService.save(orderRequest, typeOfOrderDTO).convertDTOToResponse();

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/id")
    public ResponseEntity<OrderResponse> deleteOrder(@RequestParam Long id) throws OrderNotFoundException {

        OrderResponse orderResponse = orderService.findById(id)
                .map(OrderDTO::convertDTOToResponse)
                .orElseThrow(() -> new OrderNotFoundException("There are no order in restaurant with that id: " + id));

        orderService.deleteById(id);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<OrderResponse>> getOrderByParams(@RequestParam(required = false) String date,
                                                                @RequestParam(required = false) String typeOfOrder,
                                                                @RequestParam(required = false) Long employeeId
                                                         ) {

        Stream<OrderResponse> ordersFoundByDate = orderService.findByDate(date).stream().map(OrderDTO::convertDTOToResponse);
        Stream<OrderResponse> ordersFoundByEmployee = null;
        Stream<OrderResponse> ordersFoundByTypeOfOrder = null;

        if (typeOfOrder != null) {
            TypeOfOrder typeOfOrderFound = typeOfOrderService.findByType(typeOfOrder).map(TypeOfOrderDTO::convertDTOToEntity).orElse(null);
            ordersFoundByTypeOfOrder = orderService.findByTypeOfOrder(typeOfOrderFound).stream().map(OrderDTO::convertDTOToResponse);
        }
        if (employeeId != null) {
            Employee employeeFound = employeeService.findById(employeeId).map(EmployeeDTO::convertDTOToEntity).orElse(null);
            ordersFoundByEmployee = orderService.findByEmployee(employeeFound).stream().map(OrderDTO::convertDTOToResponse);
        }

        List<OrderResponse> ordersFound = Stream.of(ordersFoundByDate, ordersFoundByTypeOfOrder, ordersFoundByEmployee)
                .flatMap(Function.identity())
                .distinct()
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderResponse>> getOrderMade() {
        List<OrderResponse> ordersFound = orderService.findMadeOrders()
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/unready")
    public ResponseEntity<List<OrderResponse>> getOrderUnmade() {
        List<OrderResponse> ordersFound = orderService.findUnmadeOrders()
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }
}