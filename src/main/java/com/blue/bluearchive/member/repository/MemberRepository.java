package com.blue.bluearchive.member.repository;

import com.blue.bluearchive.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long > {
    Member findByEmail(String email);
    Member findByIdx(Long idx);
}
