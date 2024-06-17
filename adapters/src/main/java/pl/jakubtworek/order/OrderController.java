package pl.jakubtworek.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.order.dto.OrderConfirmRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderReceiveRequest;
import pl.jakubtworek.order.dto.OrderRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderFacade orderFacade;

    OrderController(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping
    ResponseEntity<OrderDto> create(@RequestHeader("Authorization") String jwt, @RequestBody OrderRequest orderRequest) {
        logger.info("Received a request to create a new order.");
        final var result = orderFacade.save(orderRequest, jwt);
        logger.info("Order {} created successfully.", result.getId());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping ("/confirm")
    ResponseEntity<OrderDto> confirm(@RequestHeader("Authorization") String jwt, @RequestBody OrderConfirmRequest orderRequest) {
        logger.info("Received a request to confirm an order.");
        final var result = orderFacade.confirm(orderRequest, jwt);
        logger.info("Order {} confirmed successfully.", result.getId());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping("/ongoing")
    List<OrderDto> getOngoingOrders(@RequestHeader("Authorization") String jwt) {
        logger.info("Received a request for ongoing orders:");
        final var orders = orderFacade.findOngoingOrders(jwt);
        logger.info("Returned {} orders in the response.", orders.size());
        return orders;
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping("/receive")
    ResponseEntity<OrderDto> received(@RequestHeader("Authorization") String jwt, @RequestBody OrderReceiveRequest orderRequest) {
        logger.info("Received a request to receive a delivered order.");
        final var result = orderFacade.receive(orderRequest, jwt);
        logger.info("Order {} received successfully.", result.getId());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/filter")
    List<OrderDto> getOrderByParams(@RequestHeader("Authorization") String jwt,
                                    @RequestParam(required = false) String fromDate,
                                    @RequestParam(required = false) String toDate,
                                    @RequestParam(required = false) String typeOfOrder,
                                    @RequestParam(required = false) Boolean isReady,
                                    @RequestParam(required = false) Long employeeId,
                                    @RequestParam(required = false) Long customerId
    ) {
        logger.info("Received a request for filtering orders:");
        logger.info("fromDate: {}", fromDate);
        logger.info("toDate: {}", toDate);
        logger.info("typeOfOrder: {}", typeOfOrder);
        logger.info("isReady: {}", isReady);
        logger.info("employeeId: {}", employeeId);
        logger.info("customerId: {}", customerId);

        final var orders = orderFacade.findByParams(fromDate, toDate, typeOfOrder, isReady, employeeId, customerId, jwt);
        logger.info("Returned {} orders in the response.", orders.size());
        return orders;
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping
    List<OrderDto> get(@RequestHeader("Authorization") String jwt) {
        logger.info("Received a request to get the list of all orders.");
        return orderFacade.findAllByToken(jwt);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping("/{id}")
    ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        logger.info("Received a request to get order details for ID: {}", id);
        return orderFacade.findById(id)
                .map(order -> {
                    logger.info("Found order with ID {}: {}", id, order.getId());
                    return ResponseEntity.ok(order);
                })
                .orElseGet(() -> {
                    logger.warn("Order with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
