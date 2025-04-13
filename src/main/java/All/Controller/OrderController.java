package All.Controller;

import All.Dto.OrderDto;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Order;
import All.Response.ApiResponse;
import All.Service.Category.ICategoryService;
import All.Service.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${}/orders")
public class OrderController {
    @Autowired
    private final IOrderService service;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam  Long userId){
        try {
            OrderDto order=service.getOrderById(userId);
            return ResponseEntity.
                    ok(new ApiResponse("Item Order Success",order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error Occured",e.getMessage()));
        }}

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order=service.getOrderById(orderId);
            return ResponseEntity
                    .ok(new ApiResponse("Item Order Success",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));

        }}


    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> order=service.getOrderByUserID(userId);
            return ResponseEntity
                    .ok(new ApiResponse("Item Order Success",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));

        }}
















}
