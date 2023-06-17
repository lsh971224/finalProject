package com.blue.bluearchive.shop.entity;

import com.blue.bluearchive.constant.ItemUseability;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; //주문 가격

    private int count; //주문 수량

    @Enumerated(EnumType.STRING)
    private ItemUseability itemUseability; //상품 삭제 여부 디폴트 값은 사용 가능

    public static OrderItem createOrderItem(Item item, int count)
    {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }


    // 이건 하나의 항목에 대해서 전체적인 금액의 합
    public int getTotalPrice()  { return orderPrice*count; }
    public void cancel()        { this.getItem().addStock(count); }


}
