package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderResponse> ordersFound = orderService.findAll()
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        OrderResponse orderResponse = orderService.findById(id)
                .map(OrderDTO::convertDTOToResponse)
                .orElseThrow(() -> new OrderNotFoundException("There are no order in restaurant with that id: " + id));

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        OrderResponse orderResponse = orderService.save(orderRequest).convertDTOToResponse();

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) throws OrderNotFoundException {
        orderService.deleteById(id);

        return new ResponseEntity<>("Order with id: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<OrderResponse>> getOrderByParams(@RequestParam(required = false) String date,
                                                                @RequestParam(required = false) String typeOfOrder,
                                                                @RequestParam(required = false) Long employeeId
                                                         ) {
        List<OrderResponse> ordersFound = orderService.findByParams(date, typeOfOrder, employeeId)
                .stream()
                .map(OrderDTO::convertDTOToResponse)
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