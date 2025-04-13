package All.Service.Order;

import All.Dto.OrderDto;
import All.Model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getOrderByUserID(Long userId);

    OrderDto OrderToOrderDto(Order order);
}
