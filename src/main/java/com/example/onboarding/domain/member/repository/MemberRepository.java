package com.example.onboarding.domain.member.repository;

import com.example.onboarding.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
