package groom.backend.springtricount.expense;

import groom.backend.springtricount.member.MemberDto;
import groom.backend.springtricount.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ExpenseRepository {
    private final JdbcTemplate jdbcTemplate;

    public ExpenseEntity save(ExpenseEntity expenseEntity) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("expense")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "name", expenseEntity.name(),
                "settlement_id", expenseEntity.settlementId(),
                "payer_member_id", expenseEntity.payerMember().id(),
                "amount", expenseEntity.amount(),
                "expense_date_time", expenseEntity.expenseDateTime()
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(params);

        return new ExpenseEntity(
                key.longValue(),
                expenseEntity.name(),
                expenseEntity.settlementId(),
                expenseEntity.payerMember(),
                expenseEntity.amount(),
                expenseEntity.expenseDateTime());
    }

    public List<ExpenseEntity> findAll(MemberEntity member) {
        return jdbcTemplate.query("""
                        select e.id                as expense_id,
                               e.name              as expense_name,
                               e.amount            as expense_amount,
                               e.settlement_id     as expense_settlement_id,
                               e.expense_date_time as expense_expense_date_time,
                               m.id                as member_id,
                               m.login_id          as member_login_id,
                               m.name              as member_name
                        from expense e
                        inner join member m on e.payer_member_id = m.id
                        where e.payer_member_id = ?;
                        """, this::expenseRowMapper, member.id())
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ExpenseEntity> findAllBySettlementId(Long settlementId) {
        return jdbcTemplate.query("""
                        select e.id                as expense_id,
                               e.name              as expense_name,
                               e.amount            as expense_amount,
                               e.settlement_id     as expense_settlement_id,
                               e.expense_date_time as expense_expense_date_time,
                               m.id                as member_id,
                               m.login_id          as member_login_id,
                               m.name              as member_name
                        from expense e
                        inner join member m on e.payer_member_id = m.id
                        and e.settlement_id = ?;
                        """, this::expenseRowMapper, settlementId)
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ExpenseEntity> findAllBySettlementId(MemberEntity member, Long settlementId) {
        return jdbcTemplate.query("""
                        select e.id                as expense_id,
                               e.name              as expense_name,
                               e.amount            as expense_amount,
                               e.settlement_id     as expense_settlement_id,
                               e.expense_date_time as expense_expense_date_time,
                               m.id                as member_id,
                               m.login_id          as member_login_id,
                               m.name              as member_name
                        from expense e
                        inner join member m on e.payer_member_id = m.id
                        where e.payer_member_id = ?
                        and e.settlement_id = ?;
                        """, this::expenseRowMapper, member.id(), settlementId)
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    private ExpenseEntity expenseRowMapper(ResultSet rs, int rowNum) {
        try {
            return new ExpenseEntity(
                    rs.getLong("expense_id"),
                    rs.getString("expense_name"),
                    rs.getLong("expense_settlement_id"),
                    new MemberEntity(
                            rs.getLong("member_id"),
                            rs.getString("member_login_id"),
                            rs.getString("member_name"),
                            null),
                    rs.getBigDecimal("expense_amount"),
                    getExpenseDateTime(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LocalDateTime getExpenseDateTime(ResultSet rs) throws SQLException {
        return rs.getDate("expense_expense_date_time")
                .toLocalDate()
                .atTime(rs.getTime("expense_expense_date_time").toLocalTime());
    }

    public void delete(MemberEntity member, Long id) {
        jdbcTemplate.update("delete from expense where id = ? and payer_member_id = ?", id, member.id());
    }
}
