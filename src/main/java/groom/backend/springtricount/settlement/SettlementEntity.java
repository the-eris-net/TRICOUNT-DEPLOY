package groom.backend.springtricount.settlement;

import groom.backend.springtricount.member.MemberEntity;

import java.util.List;

public record SettlementEntity(
        Long id,
        String name,
        List<MemberEntity> participants
) {
}
