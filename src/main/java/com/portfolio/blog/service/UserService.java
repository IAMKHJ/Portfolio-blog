package com.portfolio.blog.service;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.user.ChangePasswordDto;
import com.portfolio.blog.dto.user.ChangeProfileDto;
import com.portfolio.blog.dto.user.LoginDto;
import com.portfolio.blog.dto.user.LoginSessionDto;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Transactional
    public MessageDto<?> login(LoginDto dto){

        Optional<Member> member = memberRepository.findByUid(dto.getUid());

        if(member.isEmpty()){ // 회원 없으면
            return new MessageDto<>("noMember");
        }

        if(passwordEncoder.matches(dto.getPassword(), member.get().getPassword())){ // 비밀번호 맞으면
            LoginSessionDto loginMember = LoginSessionDto.builder()
                    .id(member.get().getId())
                    .uid(member.get().getUid())
                    .name(member.get().getName())
                    .email(member.get().getEmail())
                    .roleType(member.get().getRoleType())
                    .files(member.get().getFiles())
                    .build();
            return new MessageDto<>("ok", loginMember);
        }

        return new MessageDto<>("differPw");
    }

    @Transactional
    public void changeProfile(ChangeProfileDto dto) throws IOException {
        Member member = memberRepository.findByUid(dto.getMemberUid())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(!member.getFiles().isEmpty()){
            if(dto.getFile().isEmpty()){//등록된 이미지가 있고 업로드하는 이미지가 없을때
                fileService.delete(member.getFiles().get(0));
            }else{//등록된 이미지가 있고 업로드하는 이미지가 있을때
                fileService.delete(member.getFiles().get(0));
                fileService.saveWithProfile(dto.getFile(), member);
            }
        }else{
            if(!dto.getFile().isEmpty()){//등록된 이미지가 없고 업로드하는 이미지가 있을때
                fileService.saveWithProfile(dto.getFile(), member);
            }
        }
    }

    @Transactional
    public MessageDto<?> changePassword(ChangePasswordDto dto) {
        Optional<Member> member = memberRepository.findById(dto.getId());

        if(member.isPresent()){// 등록된 사용자가 있으면
            if(passwordEncoder.matches(dto.getPassword(),member.get().getPassword())){// 현재 비밀번호 맞는지 확인
                member.get().changePassword(passwordEncoder.encode(dto.getChangePassword()));
                return new MessageDto<>("ok");
            }else{
                return new MessageDto<>("wrong");
            }
        }else{
            return new MessageDto<>("false");
        }
    }
}
