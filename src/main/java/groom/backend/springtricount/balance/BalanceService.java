package groom.backend.springtricount.balance;

import groom.backend.springtricount.expense.ExpenseDto;
import groom.backend.springtricount.member.MemberDto;
import groom.backend.springtricount.settlement.SettlementDto;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BalanceService {

    public List<BalanceDto> getBalance(SettlementDto settlement) {
        if (settlement == null) {
            throw new RuntimeException("정산 할 정보가 없습니다.");
        }
        List<ExpenseDto> expenses = settlement.expenses();
        List<MemberDto> participants = settlement.participants();

        BigDecimal totalAmount = getTotalAmount(expenses);
        BigDecimal averageAmount = totalAmount.divide(BigDecimal.valueOf(participants.size()), RoundingMode.DOWN);

        Map<MemberDto, BigDecimal> memberExpenseMap = getMemberExpenseMap(expenses);
        Map<MemberDto, BigDecimal> memberAmountMap = getMemberAmountMap(participants, memberExpenseMap, averageAmount);

        List<MemberBigDecimal> receiver = getMemberBigDecimals(
                memberAmountMap,
                entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0,
                Comparator.comparing(MemberBigDecimal::amount));

        List<MemberBigDecimal> sender = getMemberBigDecimals(
                memberAmountMap,
                entry -> entry.getValue().compareTo(BigDecimal.ZERO) < 0,
                Comparator.comparing(MemberBigDecimal::amount).reversed());

        return getBalances(receiver, sender);
    }

    private List<BalanceDto> getBalances(List<MemberBigDecimal> receiver, List<MemberBigDecimal> sender) {
        int receiverIndex = 0;
        int senderIndex = 0;

        List<BalanceDto> balances = new ArrayList<>();
        MemberBigDecimal receiverMember = receiver.getFirst();
        MemberBigDecimal senderMember = sender.getFirst();
        while (true) {
            BigDecimal receiverAmount = receiverMember.amount().abs();
            BigDecimal senderAmount = senderMember.amount().abs();

            BigDecimal minAmount = receiverAmount.min(senderAmount);
            receiverMember = receiverMember.withAmount(receiverMember.amount().subtract(minAmount));
            senderMember = senderMember.withAmount(senderMember.amount().add(minAmount));

            /*
             * 알고리즘상 여기까지는 무조건 실행된다.
             */
            balances.add(
                    new BalanceDto(
                            senderMember.member().id(),
                            senderMember.member().name(),
                            minAmount.abs().longValue(),
                            receiverMember.member().id(),
                            receiverMember.member().name()));

            if (receiverMember.amount().compareTo(BigDecimal.ZERO) == 0) {
                receiverIndex++;
                if (receiverIndex >= receiver.size()) break;
                receiverMember = receiver.get(receiverIndex);
            }
            if (senderMember.amount().compareTo(BigDecimal.ZERO) == 0) {
                senderIndex++;
                if (senderIndex >= sender.size()) break;
                senderMember = sender.get(senderIndex);
            }
        }
        return balances;
    }

    private static List<MemberBigDecimal> getMemberBigDecimals(
            Map<MemberDto, BigDecimal> memberAmountMap,
            Predicate<Map.Entry<MemberDto, BigDecimal>> entryPredicate, Comparator<MemberBigDecimal> comparing
    ) {
        return memberAmountMap.entrySet().stream()
                .filter(entryPredicate)
                .map(entry -> new MemberBigDecimal(entry.getKey(), entry.getValue()))
                .sorted(comparing)
                .toList();
    }

    private Map<MemberDto, BigDecimal> getMemberAmountMap(
            List<MemberDto> participants,
            Map<MemberDto, BigDecimal> memberExpenseMap,
            BigDecimal averageAmount
    ) {
        return participants.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        member -> memberExpenseMap.getOrDefault(member, BigDecimal.ZERO).subtract(averageAmount)));
    }

    private Map<MemberDto, BigDecimal> getMemberExpenseMap(List<ExpenseDto> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        ExpenseDto::payerMember,
                        Collectors.reducing(BigDecimal.ZERO, ExpenseDto::amount, BigDecimal::add)));
    }

    private BigDecimal getTotalAmount(List<ExpenseDto> expenses) {
        return expenses.stream()
                .map(ExpenseDto::amount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private record MemberBigDecimal(
            MemberDto member,
            @With BigDecimal amount
    ) {

    }
}
