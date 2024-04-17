package groom.backend.springtricount.expense;

import groom.backend.springtricount.member.MemberDto;
import groom.backend.springtricount.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseDto save(ExpenseDto expenseDto) {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                expenseDto.id(),
                expenseDto.name(),
                expenseDto.settlementId(),
                new MemberEntity(
                        expenseDto.payerMember().id(),
                        expenseDto.payerMember().loginId(),
                        expenseDto.payerMember().name(),
                        null),
                expenseDto.amount(),
                expenseDto.expenseDateTime());
        ExpenseEntity newExpense = expenseRepository.save(expenseEntity);
        return new ExpenseDto(
                newExpense.id(),
                newExpense.name(),
                newExpense.settlementId(),
                new MemberDto(
                        newExpense.payerMember().id(),
                        newExpense.payerMember().loginId(),
                        newExpense.payerMember().name(),
                        null),
                newExpense.amount(),
                newExpense.expenseDateTime());
    }

    public List<ExpenseDto> findAll(MemberDto memberDto) {
        MemberEntity member = new MemberEntity(memberDto.id(), memberDto.loginId(), memberDto.name(), null);
        return expenseRepository.findAll(member)
                .stream()
                .map(expenseEntity -> new ExpenseDto(
                        expenseEntity.id(),
                        expenseEntity.name(),
                        expenseEntity.settlementId(),
                        new MemberDto(
                                expenseEntity.payerMember().id(),
                                expenseEntity.payerMember().loginId(),
                                expenseEntity.payerMember().name(),
                                null),
                        expenseEntity.amount(),
                        expenseEntity.expenseDateTime()))
                .toList();
    }

    public List<ExpenseDto> findAllBySettlementId(MemberDto memberDto, Long settlementId) {
        MemberEntity member = new MemberEntity(memberDto.id(), memberDto.loginId(), memberDto.name(), null);
        return expenseRepository.findAllBySettlementId(member, settlementId)
                .stream()
                .map(expenseEntity -> new ExpenseDto(
                        expenseEntity.id(),
                        expenseEntity.name(),
                        expenseEntity.settlementId(),
                        new MemberDto(
                                expenseEntity.payerMember().id(),
                                expenseEntity.payerMember().loginId(),
                                expenseEntity.payerMember().name(),
                                null),
                        expenseEntity.amount(),
                        expenseEntity.expenseDateTime()))
                .toList();
    }

    public List<ExpenseDto> delete(MemberDto memberDto, Long id) {
        MemberEntity member = new MemberEntity(memberDto.id(), memberDto.loginId(), memberDto.name(), null);
        expenseRepository.delete(member, id);
        return expenseRepository.findAll(member)
                .stream()
                .map(expenseEntity -> new ExpenseDto(
                        expenseEntity.id(),
                        expenseEntity.name(),
                        expenseEntity.settlementId(),
                        new MemberDto(
                                expenseEntity.payerMember().id(),
                                expenseEntity.payerMember().loginId(),
                                expenseEntity.payerMember().name(),
                                null),
                        expenseEntity.amount(),
                        expenseEntity.expenseDateTime()))
                .toList();
    }
}
