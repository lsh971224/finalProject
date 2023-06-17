package com.blue.bluearchive.shop.dto;

import com.blue.bluearchive.constant.ItemUseability;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long id;
    private String itemNm;
    private int price;
    private String itemDetail;
    private String sellStatCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
	private String sellerName;
    private ItemUseability itemUseability; //상품 삭제 여부 디폴트 값은 사용 가능
}
