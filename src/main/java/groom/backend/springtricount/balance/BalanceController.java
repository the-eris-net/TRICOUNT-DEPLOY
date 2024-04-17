package groom.backend.springtricount.balance;

import groom.backend.springtricount.annotation.Login;
import groom.backend.springtricount.member.MemberDto;
import groom.backend.springtricount.settlement.SettlementDto;
import groom.backend.springtricount.settlement.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balances")
public class BalanceController {
    private final SettlementService settlementService;
    private final BalanceService balanceService;

    @GetMapping("/settlements/{id}")
    public List<BalanceDto> getAll(@Login MemberDto member, @PathVariable("id") Long settlementId){
        SettlementDto settlement = settlementService.findById(member, settlementId);
        return balanceService.getBalance(settlement);
    }
}
