package az.unibank.unitech.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(1, "Success operation"),
    INSUFFICIENT(601, "Not enough money on balance."),
    SAME_ACCOUNT(602, "You can't make transfer to the same account."),
    DEACTIVATED(603, "You can't make transfer to deactivated account."),
    ACCOUNT_NOT_EXIST(604, "You can't make transfer to non existing account."),
    NOT_YOUR_ACCOUNT_TRANSFER(605, "It's not your account, you can't transfer from foreign account."),
    ACCOUNT_NOT_FOUND(605, "Account not found..."),
    SMALL_AMOUNT(606, "Transfer amount must be larger than 1 AZN."),
    CURRENCY_NOT_FOUND(701, "Currency not found. Use USD/AZN/RUB/EUR/TRY."),
    SAME_PIN_ERROR(801, "There is a user with same pin."),
    USER_NOT_FOUND(802, "User not found."),
    AUTH_ERROR(803, "Authorization token is bad."),
    BAD_CREDENTIALS(804, "Bad credentials."),
    UNKNOWN_ERROR(-1, "Unknown error.");

    private final Integer code;

    private final String message;
}
