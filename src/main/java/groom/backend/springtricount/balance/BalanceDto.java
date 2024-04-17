package groom.backend.springtricount.balance;

public record BalanceDto(
        Long senderUserNo,
        String senderUserName,
        Long senderAmount,
        Long receiverUserNo,
        String receiverUserName
) {
}
