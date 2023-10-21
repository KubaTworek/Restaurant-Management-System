package pl.jakubtworek.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final OrderFacade orderFacade;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    OrderController(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    ResponseEntity<OrderDto> create(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String jwt) {
        OrderDto result = orderFacade.save(orderRequest, jwt);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/filter")
    List<OrderDto> getOrderByParams(@RequestParam(required = false) String fromDate,
                                    @RequestParam(required = false) String toDate,
                                    @RequestParam(required = false) String typeOfOrder,
                                    @RequestParam(required = false) Boolean isReady,
                                    @RequestParam(required = false) Long employeeId,
                                    @RequestParam(required = false) String username
    ) {
        logger.info("Received a request for filtering orders:");
        logger.info("fromDate: {}", fromDate);
        logger.info("employeeId: {}", employeeId);
        logger.info("toDate: {}", toDate);
        logger.info("typeOfOrder: {}", typeOfOrder);
        logger.info("isReady: {}", isReady);
        logger.info("username: {}", username);

        List<OrderDto> orders = orderFacade.findByParams(fromDate, toDate, typeOfOrder, isReady, employeeId, username);
        logger.info("Returned {} orders in the response.", orders.size());
        return orders;
    }

    @GetMapping
    List<OrderDto> get(@RequestHeader("Authorization") String jwt) {
        return orderFacade.findAll(jwt);
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return orderFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}