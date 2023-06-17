package com.blue.bluearchive.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

@Getter @Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price)
    {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
