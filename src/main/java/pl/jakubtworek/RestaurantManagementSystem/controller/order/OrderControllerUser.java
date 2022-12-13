package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerUser {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        OrderResponse orderResponse = orderService.save(orderRequest).convertDTOToResponse();

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID id) throws OrderNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.deleteByIdAndUsername(id, username);

        return new ResponseEntity<>("Order with id: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderResponse> ordersFound = orderService.findAllByUsername(username)
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .map(OrderResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) throws OrderNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderResponse orderResponse = orderService.findByIdAndUsername(id, username)
                .map(OrderDTO::convertDTOToResponse)
                .map(OrderResponse::addLinkToResponse)
                .orElseThrow(() -> new OrderNotFoundException("There are no order in restaurant with that id: " + id));


        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderResponse>> getOrderMade() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderResponse> ordersFound = orderService.findMadeOrdersAndUsername(username)
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .map(OrderResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }

    @GetMapping("/unready")
    public ResponseEntity<List<OrderResponse>> getOrderUnmade() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderResponse> ordersFound = orderService.findUnmadeOrdersAndUsername(username)
                .stream()
                .map(OrderDTO::convertDTOToResponse)
                .map(OrderResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ordersFound, HttpStatus.OK);
    }
}