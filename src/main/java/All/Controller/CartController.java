package All.Controller;

import All.Exceptions.ResourceNotFoundException;
import All.Model.Cart;
import All.Response.ApiResponse;
import All.Service.CartService.ICartService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {

    @Autowired
    private final ICartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
        try {
            Cart cart=cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Success",cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }
@DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        cartService.clearCart(cartId);
        return ResponseEntity
                .ok(new ApiResponse("Clear Cart Success!",null));
    }

    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartIId){
        try {
            BigDecimal totalPRice=cartService.getTotalPrice(cartIId);
            return ResponseEntity
                    .ok(new ApiResponse("Total Price",totalPRice));
        } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse(e.getMessage(),null));
        }
    }





}
