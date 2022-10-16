package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class OrderController {
    private final OrderService orderService;
    private final TypeOfOrderService typeOfOrderService;
    private final EmployeeService employeeService;

    public OrderController(OrderService orderService, TypeOfOrderService typeOfOrderService, EmployeeService employeeService) {
        this.orderService = orderService;
        this.typeOfOrderService = typeOfOrderService;
        this.employeeService = employeeService;
    }

    @GetMapping("/orders")
    public ResponseEntity<Response<List<OrderDTO>>> getOrders() throws OrderNotFoundException {
        Response<List<OrderDTO>> response = new Response<>();
        List<Order> orders = orderService.findAll();

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("There are no orders in restaurant");
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

        orderDTOS.forEach(dto -> {
            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }
        });


        response.setData(orderDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/id/{orderId}")
    public ResponseEntity<Response<OrderDTO>> getOrderById(@PathVariable Long orderId) throws OrderNotFoundException {
        Response<OrderDTO> response = new Response<>();
        Optional<Order> order = orderService.findById(orderId);

        if(order.isPresent()) {
            Order orderFound = order.get();
            OrderDTO dto = orderFound.convertEntityToDTO();

            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }

            response.setData(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new OrderNotFoundException("There are order in restaurant with that id: " + orderId);
    }

    @PostMapping("/{typeOfOrder}/order")
    public ResponseEntity<Response<OrderDTO>> saveOrder(@PathVariable String typeOfOrder, @RequestBody OrderDTO dto, BindingResult result) throws OrderNotFoundException {

        Response<OrderDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        if(typeOfOrderService.findByType(typeOfOrder).isPresent()){
            dto.setId(Long.parseLong("0"));
            dto.setTypeOfOrder(typeOfOrderService.findByType(typeOfOrder).get().convertEntityToDTO());
            Order order = orderService.save(dto.convertDTOToEntity());
            OrderDTO orderDTO = order.convertEntityToDTO();
            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }
            response.setData(orderDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        throw new OrderNotFoundException("There are no type of orders: " + typeOfOrder);
    }


    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Response<String>> deleteOrder(@PathVariable Long orderId) throws OrderNotFoundException {
        if(orderService.findById(orderId).isPresent()){
            orderService.deleteById(orderId);

            Response<String> response = new Response<>();

            response.setData("Deleted order is - " + orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new OrderNotFoundException("Order id not found - " + orderId);
    }

    @GetMapping("/orders/date/{date}")
    public ResponseEntity<Response<List<OrderDTO>>> getOrderByDate(@PathVariable String date) throws Exception {
        Response<List<OrderDTO>> response = new Response<>();
        List<Order> orders = orderService.findByDate(date);

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("There are no orders in restaurant");
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

        orderDTOS.forEach(dto -> {
            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }
        });

        response.setData(orderDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/typeOfOrder/{typeOfOrder}")
    public ResponseEntity<Response<List<OrderDTO>>> getOrderByTypeofOrder(@PathVariable String typeOfOrder) throws Exception {

        if (typeOfOrderService.findByType(typeOfOrder).isPresent()) {
            Response<List<OrderDTO>> response = new Response<>();
            List<Order> orders = orderService.findByTypeOfOrder(typeOfOrderService.findByType(typeOfOrder).get());

            List<OrderDTO> orderDTOS = new ArrayList<>();
            orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

            orderDTOS.forEach(dto -> {
                dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
                for(EmployeeDTO e: dto.getEmployees()){
                    e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
                }
            });

            response.setData(orderDTOS);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new JobNotFoundException("There are no order in restaurant with that type: " + typeOfOrder);
    }

    @GetMapping("/orders/employee/{employeeId}")
    public ResponseEntity<Response<List<OrderDTO>>> getOrderByEmployee(@PathVariable Long employeeId) throws Exception {

        if (employeeService.findById(employeeId).isPresent()) {
            Response<List<OrderDTO>> response = new Response<>();
            List<Order> orders = orderService.findByEmployee(employeeService.findById(employeeId).get());

            List<OrderDTO> orderDTOS = new ArrayList<>();
            orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

            orderDTOS.forEach(dto -> {
                dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
                for(EmployeeDTO e: dto.getEmployees()){
                    e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
                }
            });

            response.setData(orderDTOS);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new JobNotFoundException("There are no order in restaurant made by employee with id: " + employeeId);
    }

    @GetMapping("/orders/ready")
    public ResponseEntity<Response<List<OrderDTO>>> getOrderMade() throws Exception {
        Response<List<OrderDTO>> response = new Response<>();
        List<Order> orders = orderService.findMadeOrders();

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("There are no orders in restaurant");
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

        orderDTOS.forEach(dto -> {
            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }
        });

        response.setData(orderDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/unready")
    public ResponseEntity<Response<List<OrderDTO>>> getOrderUnmade() throws Exception {
        Response<List<OrderDTO>> response = new Response<>();
        List<Order> orders = orderService.findUnmadeOrders();

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("There are no orders in restaurant");
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(o -> orderDTOS.add(o.convertEntityToDTO()));

        orderDTOS.forEach(dto -> {
            dto.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("order/id").slash(dto.getId()).withSelfRel());
            for(EmployeeDTO e: dto.getEmployees()){
                e.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash("employee").slash(e.getId()).withSelfRel());
            }
        });

        response.setData(orderDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
