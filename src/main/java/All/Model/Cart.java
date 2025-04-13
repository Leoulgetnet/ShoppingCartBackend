package All.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount=BigDecimal.ZERO;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartItem=new HashSet<>();

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;



    public void addItem(CartItem item){
        item.setCart(this);/*this-->means the Cart.class*/
        this.cartItem.add(item);/*this-->means the Cart.class*/
        updateTotalAmount();/*Which is calculating the set again*/
    }

    public void removeItem(CartItem item){
        this.cartItem.remove(item);/*way of removing from the set*/
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount(){
        this.totalAmount=this.cartItem.stream()
                .map(
                        item->{
                            /*here it is better to use item.getUnitprice() than of muliplying the two properties unless
                            * there is better reason to do it*/
                            return item.getUnitPrice()==null?BigDecimal.ZERO:item.getUnitPrice().multiply(BigDecimal
                                    .valueOf(item.getQuantity()));})
                .reduce(BigDecimal.ZERO,BigDecimal::add);}}




/*@Transactional ->we use this method when we are doing a lot of
* database actions in a function or any then it set it to , if one fails every of the action
* will fail*/

//public List<SimpleGrantedAuthority> getAuthorities(){
//    var authorities=getPermission()
//            .stream()
//            .map(permission->new SimpleGrantedAuthority(permission.name()))
//            .toList();
//    authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
//    /*Role.ADMIN.getAuthorities(),so here this returns the "ADMIN" here which is this.name*/
//    return authorities;
//}




/*
 * Reduce function->Imagine you have a list of numbers,
 * and you want to find the total sum. The reduce function helps you do this by taking
 * each number in the list, one by one, and adding it to a running total.
 *
 *Stream: prices.stream() converts the list of prices into a stream of BigDecimal values.
 * Reduce: .reduce(BigDecimal.ZERO, BigDecimal::add) takes the starting value (BigDecimal.ZERO)
 * and adds each value in the stream to the total using the add method.
 *
 *
 * The map method in Java Streams is incredibly powerful and flexible. It allows you to transform
 * each element in a stream into another object or value, and the result can be of any data type.
 * This is one of the key features that makes streams so useful for data processing.
 * *
* */