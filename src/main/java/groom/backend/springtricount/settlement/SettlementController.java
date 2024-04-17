package groom.backend.springtricount.settlement;

import groom.backend.springtricount.annotation.Login;
import groom.backend.springtricount.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settlements")
public class SettlementController {
    private final SettlementService settlementService;

    @GetMapping("")
    public List<SettlementDto> getAll(@Login MemberDto member){
        return settlementService.findAll(member);
    }

    @GetMapping("/{id}")
    public SettlementDto getOne(@PathVariable("id") Long id, @Login MemberDto member){
        return settlementService.findById(member, id);
    }

    @PostMapping("/{id}/participants")
    public SettlementDto addParticipant(@PathVariable("id") Long id, @Login MemberDto member){
        return settlementService.addParticipant(member, id);
    }

    @DeleteMapping("/{id}/participants")
    public SettlementDto removeParticipant(@PathVariable("id") Long id, @Login MemberDto member){
        return settlementService.removeParticipant(member, id);
    }
}
