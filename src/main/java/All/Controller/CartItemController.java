package All.Controller;

import All.Exceptions.ResourceNotFoundException;
import All.Model.Cart;
import All.Model.User;
import All.Response.ApiResponse;
import All.Service.CartItemService.ICartItemService;
import All.Service.CartService.ICartService;
import All.Service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.boot.model.internal.CreateKeySecondPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cartItems")
@RequiredArgsConstructor
public class CartItemController {
    @Autowired
    ICartItemService service;

    @Autowired
    UserService userService;

    @Autowired
    ICartService cartService;




  @PostMapping("/item/add")
    public ResponseEntity<ApiResponse>
  addItemCart(@RequestParam Long productId
            ,@RequestParam Integer quantity){
        try {
           User user=userService.getUserById(1L);
            Cart cart=cartService.initializeNewCart(user);
            service.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity
                    .ok(new ApiResponse("Add Item Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }}


    @DeleteMapping("/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId
            ,@PathVariable Long productId){
        try {
            service.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success",null));
        } catch (Exception e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }}

    @PutMapping("/cart/{cartId}/item//{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity
            (@PathVariable Long cartId
                    ,@PathVariable Long itemId
                    ,@RequestParam Integer quantity){
        try {
            service.updateItemQuantity(cartId,cartId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success",null));
        } catch (Exception e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }}




}
