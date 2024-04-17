package groom.backend.springtricount.settlement;

import groom.backend.springtricount.expense.ExpenseDto;
import groom.backend.springtricount.member.MemberDto;

import java.util.List;

public record SettlementDto(
        Long id,
        String name,
        List<MemberDto> participants,
        List<ExpenseDto> expenses
) {
}
