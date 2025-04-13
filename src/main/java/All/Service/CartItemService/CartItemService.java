package All.Service.CartItemService;

import All.Exceptions.ResourceNotFoundException;
import All.Model.Cart;
import All.Model.CartItem;
import All.Model.Product;
import All.Repository.CartItemRepository;
import All.Repository.CartRepository;
import All.Service.CartService.CartService;
import All.Service.Product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartRepository cartRepo;
    private final CartItemRepository repo;
    private final IProductService productService;
    private final CartService cartService;

    AtomicLong atomicLong=new AtomicLong(8);
    @Override
    @Transactional
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {throw new ResourceNotFoundException("Cart not found with id: " + cartId);}
        Product product = productService.getProductById(productId);
        if (product == null) {throw new ResourceNotFoundException("Product not found with id: " + productId);}
        /*Avoids dublication*/
        CartItem cartItem = cart.getCartItem().stream()
                .filter(item -> item.getProduct().equals(product)) //only the data's which satisfy the condition will pass through
                .findFirst()  /*It returns the first element of the stream,If the stream is empty, it returns an empty Optional*/
                .orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }



        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        repo.save(cartItem);
        cartRepo.save(cart);}


    @Override
    @Transactional
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        if (itemToRemove != null) {
            cart.removeItem(itemToRemove);
            cartRepo.save(cart);
        }}

    @Override
    @Transactional
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItem()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().ifPresent(item -> {
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setQuantity(quantity);
                    item.setTotalPrice();
                });

        BigDecimal totalAmount = cart.getCartItem().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepo.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItem().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item Not Found"));
    }

}
