//package com.blue.bluearchive.shop_inquiry.entity;
//
//import com.blue.bluearchive.shop.entity.BaseEntity;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "inquiry_answer")
//@Getter
//@Setter
//public class InquiryAnswer extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "inquiry_answer_id")
//    private int id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inquiry_id")
//    private Inquiry inquiry;
//
//    @Column(name = "inquiry_answer_content", nullable = false)
//    private String content;
//
//}
