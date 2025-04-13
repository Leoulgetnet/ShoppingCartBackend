package All.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public OrderItem(Order order,Product product,int quantity,  BigDecimal price) {
        this.product = product;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
    }
}
