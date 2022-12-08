package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final MenuItemService menuItemService;
    private final UserService userService;

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
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody OrderRequest orderRequest) throws TypeOfOrderNotFoundException {

        TypeOfOrderDTO typeOfOrderDTO = typeOfOrderService.findByType(orderRequest.getTypeOfOrder())
                .orElseThrow(TypeOfOrderNotFoundException::new);
        UserDTO userDTO = userService.findByUsername(orderRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the user: " + orderRequest.getUsername()));
        List<MenuItemDTO> menuItems = new ArrayList<>();
        for(MenuItemRequest miRequests : orderRequest.getMenuItems()){
            menuItems.add(menuItemService.findByName(miRequests.getName()).orElseThrow());
        }

        OrderResponse orderResponse = orderService.save(orderRequest, typeOfOrderDTO, menuItems, userDTO).convertDTOToResponse();

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable Long id) throws OrderNotFoundException {

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
            ordersFoundByEmployee = orderService.findByEmployeeId(employeeId).stream().map(OrderDTO::convertDTOToResponse);
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