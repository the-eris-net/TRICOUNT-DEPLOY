package groom.backend.springtricount.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto signup(MemberDto memberDto) {
        MemberEntity member = new MemberEntity(null, memberDto.loginId(), memberDto.name(), memberDto.password());
        MemberEntity newMember = memberRepository.save(member);
        return new MemberDto(newMember.id(), newMember.loginId(), newMember.name(), null);
    }

    public List<MemberDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(memberEntity -> new MemberDto(memberEntity.id(), memberEntity.loginId(), memberEntity.name(), null))
                .toList();
    }

    public MemberDto login(String loginId, String password) {
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .filter(m -> m.password().equals(password))
                .orElseThrow(() -> new RuntimeException("Member info is not found!"));
        return new MemberDto(member.id(), member.loginId(), member.name(), null);
    }
}
