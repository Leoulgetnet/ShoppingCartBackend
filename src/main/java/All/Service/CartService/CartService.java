package All.Service.CartService;

import All.Exceptions.ResourceNotFoundException;
import All.Model.Cart;
import All.Model.User;
import All.Repository.CartItemRepository;
import All.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Repository
public class CartService implements ICartService{
    @Autowired
   private CartRepository repo;
    @Autowired
    private CartItemRepository itemrepo;

    private final AtomicLong cartIdGenerator=new AtomicLong(8);


    @Override
    public Cart getCart(Long id) {
        Cart cart=repo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Cart Not Found"));
        BigDecimal totalAmount=cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return repo.save(cart);}

    @Override
    public Cart getCartByUserId(Long id){
        return repo.getByUserId(id);}


    @Override
    public void clearCart(Long id) {
Cart cart=getCart(id);
itemrepo.deleteAllByCartId(id);
cart.getCartItem();
repo.deleteById(id);}



    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }
    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart=new Cart();
                    cart.setUser(user);
                    return repo.save(cart);
                });
    }


}
