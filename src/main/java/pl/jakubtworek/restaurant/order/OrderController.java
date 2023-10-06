package pl.jakubtworek.restaurant.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final OrderService orderService;

    OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    ResponseEntity<OrderDto> create(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String jwt) {
        OrderDto result = orderService.save(orderRequest, jwt);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    List<OrderDto> getOrderByParams(@RequestParam(required = false) String date,
                                    @RequestParam(required = false) String typeOfOrder,
                                    @RequestParam(required = false) Boolean isReady,
                                    @RequestParam(required = false) Long employeeId,
                                    @RequestParam(required = false) String username
    ) {
        return orderService.findByParams(date, typeOfOrder, isReady, employeeId, username);
    }

    @GetMapping
    List<OrderDto> get(@RequestHeader("Authorization") String jwt) {
        return orderService.findAll(jwt);
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}