package All.Dto;

import All.Model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
private Long itemId;
private Integer quantity;
private BigDecimal unitPrice;
private ProductDto product;

}
