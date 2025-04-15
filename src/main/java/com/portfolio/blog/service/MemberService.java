package com.portfolio.blog.service;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.member.MemberDetailDto;
import com.portfolio.blog.dto.member.MemberSaveDto;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.common.RoleType;
import com.portfolio.blog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MessageDto<?> save(MemberSaveDto dto){

        Optional<Member> member = memberRepository.findByUid(dto.getUid());
        if(member.isPresent()) return new MessageDto<>("duplicateUid");

        if(dto.getRoleType().equals(RoleType.ADMIN)){
            Optional<Member> email = memberRepository.findByEmail(dto.getEmail());
            if(email.isPresent()) return new MessageDto<>("duplicateEmail");
        }

        Member newMember = Member.builder()
                .uid(dto.getUid())
                .password(passwordEncoder.encode(dto.getPassword())) // 비밀번호 암호화
                .name(dto.getName())
                .email(dto.getEmail())
                .roleType(dto.getRoleType().equals(RoleType.ADMIN)?RoleType.ADMIN:RoleType.USER)
                .build();

        memberRepository.save(newMember);

        return new MessageDto<>("ok", newMember.getName());
    }

    @Transactional(readOnly = true)
    public MemberDetailDto findByUid(String uid){
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return MemberDetailDto.builder()
                .id(member.getId())
                .uid(member.getUid())
                .name(member.getName())
                .email(member.getEmail())
                .roleType(member.getRoleType())
                .file(member.getFiles().isEmpty()?null:member.getFiles().get(0))
                .build();
    }
}
