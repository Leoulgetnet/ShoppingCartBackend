package All.Service.CartService;

import All.Model.Cart;
import All.Model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    Cart getCartByUserId(Long id);

    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);
}
