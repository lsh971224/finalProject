package com.blue.bluearchive.member.entity;

import com.blue.bluearchive.constant.Grade;
import com.blue.bluearchive.constant.MemberStat;
import com.blue.bluearchive.constant.Role;
import com.blue.bluearchive.shop.entity.BaseEntity;
import com.blue.bluearchive.member.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    private String id;

    private String name;

    private String nickName;

    @Column(unique=true)
    private String email;

    private String password;

    private String address;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.BRONZE;

    @Enumerated(EnumType.STRING)
    private MemberStat memberStat = MemberStat.MEMBER;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setId(memberFormDto.getId());
        String password=passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        member.setNickName(memberFormDto.getNickName());
        member.setPhoneNum(memberFormDto.getPhoneNum());
        return member;
    }
}
