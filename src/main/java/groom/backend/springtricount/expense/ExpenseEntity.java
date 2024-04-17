package groom.backend.springtricount.expense;

import groom.backend.springtricount.member.MemberEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseEntity(
        Long id,
        String name,
        Long settlementId,
        MemberEntity payerMember,
        BigDecimal amount,
        LocalDateTime expenseDateTime
) {
}
