package All.Request;

import All.Model.Category;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductUpdateRequest {
    private int id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
