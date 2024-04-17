package groom.backend.springtricount.settlement;

import groom.backend.springtricount.member.MemberDto;
import groom.backend.springtricount.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<SettlementEntity> findAllByMemberId(MemberEntity member) {
        return jdbcTemplate.query("""
                                select s.id       as settlement_id,
                                       s.name     as settlement_name,
                                       m.id       as member_id,
                                       m.login_id as member_login_id,
                                       m.name     as member_name
                                from (select * from settlement_participant where member_id = ?) spm
                                         join settlement_participant sp on spm.settlement_id = sp.settlement_id
                                         join settlement s on s.id = sp.settlement_id
                                         join member m on sp.member_id = m.id
                                """,
                        this::settlementByMemberIdRowMapper,
                        member.id()
                ).stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.groupingBy(
                                SettlementEntity::id,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        this::mergeMemberEntityBySettlementEntity
                                )))
                .values().stream().toList();
    }

    public Optional<SettlementEntity> findByMemberId(MemberEntity member, Long id) {
        return jdbcTemplate.query("""
                                select s.id       as settlement_id,
                                       s.name     as settlement_name,
                                       m.id       as member_id,
                                       m.login_id as member_login_id,
                                       m.name     as member_name
                                from (select * from settlement_participant where member_id = ?) spm
                                         join settlement_participant sp on spm.settlement_id = sp.settlement_id
                                         join settlement s on s.id = sp.settlement_id
                                         join member m on sp.member_id = m.id
                                where s.id = ?
                                """,
                        this::settlementByMemberIdRowMapper,
                        member.id(),
                        id
                ).stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.groupingBy(
                                SettlementEntity::id,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        this::mergeMemberEntityBySettlementEntity
                                )))
                .values().stream().findFirst();
    }

    public Optional<SettlementEntity> findById(Long id) {
        return jdbcTemplate.query("""
                                select s.id       as settlement_id,
                                       s.name     as settlement_name,
                                       m.id       as member_id,
                                       m.login_id as member_login_id,
                                       m.name     as member_name
                                from settlement_participant sp
                                         join settlement s on s.id = sp.settlement_id
                                         join member m on sp.member_id = m.id
                                where s.id = ?
                                """,
                        this::settlementByMemberIdRowMapper,
                        id
                ).stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.groupingBy(
                                SettlementEntity::id,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        this::mergeMemberEntityBySettlementEntity
                                )))
                .values().stream().findFirst();
    }

    /**
     * SettlementEntity를 List로 받아서 participants를 합쳐서 하나의 SettlementEntity로 만들어준다.
     */
    private SettlementEntity mergeMemberEntityBySettlementEntity(List<SettlementEntity> list) {
        SettlementEntity settlement = list.getFirst();
        List<MemberEntity> participants = list.stream()
                .flatMap(s -> s.participants().stream())
                .toList();
        return new SettlementEntity(settlement.id(), settlement.name(), participants);
    }

    private SettlementEntity settlementByMemberIdRowMapper(ResultSet rs, int rowNum) {
        try {
            return new SettlementEntity(
                    rs.getLong("settlement_id"),
                    rs.getString("settlement_name"),
                    List.of(
                            new MemberEntity(
                                    rs.getLong("member_id"),
                                    rs.getString("member_login_id"),
                                    rs.getString("member_name"),
                                    null
                            )));
        } catch (SQLException e) {
            return null;
        }
    }

    public Optional<SettlementEntity> addParticipant(MemberDto member, Long id) {
        jdbcTemplate.update("INSERT INTO settlement_participant (settlement_id, member_id) VALUES (?, ?)",
                id, member.id());
        return findById(id);
    }

    public void removeParticipant(MemberDto member, Long id) {
        jdbcTemplate.update("DELETE FROM settlement_participant WHERE settlement_id = ? AND member_id = ?",
                id, member.id());
    }
}


