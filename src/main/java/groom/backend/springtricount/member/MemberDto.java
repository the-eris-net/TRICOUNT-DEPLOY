package groom.backend.springtricount.member;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public record MemberDto(
        Long id,
        String loginId,
        String name,
        @JsonIgnore String password
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(id, memberDto.id) && Objects.equals(loginId, memberDto.loginId) && Objects.equals(name, memberDto.name) && Objects.equals(password, memberDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loginId, name, password);
    }
}
