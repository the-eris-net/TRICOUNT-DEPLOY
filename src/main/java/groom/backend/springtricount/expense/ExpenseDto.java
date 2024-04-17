package groom.backend.springtricount.expense;

import groom.backend.springtricount.member.MemberDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseDto(
        Long id,
        String name,
        Long settlementId,
        MemberDto payerMember,
        BigDecimal amount,
        LocalDateTime expenseDateTime
) {
}
