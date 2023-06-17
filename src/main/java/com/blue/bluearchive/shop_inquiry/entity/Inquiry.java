package com.blue.bluearchive.shop_inquiry.entity;

import com.blue.bluearchive.shop.entity.BaseEntity;
import com.blue.bluearchive.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "inquiry_content", nullable = false)
    private String content;

}
