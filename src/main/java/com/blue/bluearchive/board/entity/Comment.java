package com.blue.bluearchive.board.entity;

import com.blue.bluearchive.shop.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "comment_content", length = 300, nullable = false)
    private String commentContent;

    @Column(name = "comment_likeCount")
    private Integer commentLikeCount = 0;

    @Column(name = "comment_hateCount")
    private Integer commentHateCount = 0;

    @Column(name = "comment_reportsCount")
    private Integer commentReportsCount = 0;


}