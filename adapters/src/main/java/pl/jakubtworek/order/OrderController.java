package pl.jakubtworek.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.OrderResponse;

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

    @PostMapping
    ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String jwt) {
        logger.info("Received a request to create a new order.");
        final var result = orderFacade.save(orderRequest, jwt);
        logger.info("Order {} created successfully.", result.id());
        return ResponseEntity.created(URI.create("/" + result.id())).body(result);
    }

    @GetMapping("/filter")
    List<OrderDto> getOrderByParams(@RequestParam(required = false) String fromDate,
                                    @RequestParam(required = false) String toDate,
                                    @RequestParam(required = false) String typeOfOrder,
                                    @RequestParam(required = false) Boolean isReady,
                                    @RequestParam(required = false) Long employeeId,
                                    @RequestParam(required = false) Long userId
    ) {
        logger.info("Received a request for filtering orders:");
        logger.info("fromDate: {}", fromDate);
        logger.info("toDate: {}", toDate);
        logger.info("typeOfOrder: {}", typeOfOrder);
        logger.info("isReady: {}", isReady);
        logger.info("employeeId: {}", employeeId);
        logger.info("userId: {}", userId);

        final var orders = orderFacade.findByParams(fromDate, toDate, typeOfOrder, isReady, employeeId, userId);
        logger.info("Returned {} orders in the response.", orders.size());
        return orders;
    }

    @GetMapping
    List<OrderDto> get(@RequestHeader("Authorization") String jwt) {
        logger.info("Received a request to get the list of all orders.");
        return orderFacade.findAllByToken(jwt);
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        logger.info("Received a request to get order details for ID: {}", id);
        return orderFacade.findById(id)
                .map(order -> {
                    logger.info("Found order with ID {}: {}", id, order.id());
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
