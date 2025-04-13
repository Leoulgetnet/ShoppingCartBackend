package All.Service.Order;

import All.Dto.OrderDto;
import All.Enums.OrderStatus;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Cart;
import All.Model.Order;
import All.Model.OrderItem;
import All.Model.Product;
import All.Repository.OrderRepository;
import All.Repository.ProductRepository;
import All.Service.CartService.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    @Autowired
private final OrderRepository repo;
    @Autowired
private final ProductRepository productRepo;

    @Autowired
    private final CartService cartService;


    private final ModelMapper mapper;


    @Override
    public Order placeOrder(Long userId) {
Cart cart=cartService.getCart(userId);
Order order=createorder(cart);
List<OrderItem> orderItemList=createOrderItems(order,cart);
order.setOrderItems(new HashSet<>(orderItemList));
order.setTotalAmount(calculateTotalAmount(orderItemList));
Order saveOrder=repo.save(order);
cartService.clearCart(cart.getId());
        return saveOrder;
    }

    private Order createorder(Cart cart){
        Order order=new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;}



    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItem().stream()
                .map(
                        cartitem->{
                            Product product= cartitem.getProduct();
                            product.setInventory(product.getInventory()-cartitem.getQuantity());
                            productRepo.save(product);
                            return new OrderItem(
                                    order,
                                    product,
                                    cartitem.getQuantity(),
                                    cartitem.getUnitPrice());}
                ).toList();}


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList
                .stream()
                .map(item->item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }








    @Override
    public OrderDto getOrderById(Long orderId) {
       return repo.findById(orderId).map(this::OrderToOrderDto)
               .orElseThrow(()->new ResourceNotFoundException("Not Found"));
    }

    @Override
    public List<OrderDto> getOrderByUserID(Long userId) {
        List<OrderDto> orderDtos=repo.findByUserId(userId)
                .stream().map(this::OrderToOrderDto).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public OrderDto OrderToOrderDto(Order order){
        return mapper.map(order,OrderDto.class);
    }





}
