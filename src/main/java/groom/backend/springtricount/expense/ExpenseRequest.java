package groom.backend.springtricount.expense;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ExpenseRequest(
        @NotNull String name,
        @NotNull Long settlementId,
        Long payerMemberId,
        @NotNull BigDecimal amount,
        LocalDateTime expenseDateTime
) {

}
