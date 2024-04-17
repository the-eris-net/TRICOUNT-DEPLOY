package groom.backend.springtricount.member;

import groom.backend.springtricount.annotation.Login;
import groom.backend.springtricount.member.request.LoginRequest;
import groom.backend.springtricount.member.request.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/members")
    public List<MemberDto> findAll() {
        return memberService.findAll();
    }

    @PostMapping("/signup")
    public MemberDto signup(@Valid @RequestBody SignupRequest signupRequest) {
        MemberDto memberDto = new MemberDto(null, signupRequest.loginId(), signupRequest.name(), signupRequest.password());
        return memberService.signup(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            HttpServletRequest request,
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        MemberDto member = memberService.login(loginRequest.loginId(), loginRequest.password());
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            @Login MemberDto member
    ) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
