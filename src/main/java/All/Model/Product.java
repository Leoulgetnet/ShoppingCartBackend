package All.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    @ManyToOne(cascade = ALL) /*The foreign key will get deleted*/
    @JoinColumn(name="category_id")
    private Category category;

@OneToMany(mappedBy = "product",cascade= ALL,orphanRemoval = true)/*When product is deleted products associated also deleted + Foreign key*/
    private List<Image> images;

    public Product(Category category, int inventory, BigDecimal price, String description, String brand, String name) {
        this.category = category;
        this.inventory = inventory;
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.name = name;
    }
}


