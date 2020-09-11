package utils;

import interfaces.Payable;

public class ValidationExceptionMessageHandler {
    final public static String UNSUPPORTED_PAYABLE_TYPE_MESSAGE = "Sorry ${PAYABLE_TYPE} ${PAYABLE}(s) are NOT Supported";
    final public static String INVALID_PAYABLE_ENTERED_MESSAGE = "Please Insert ${PAYABLE}(s) ONLY in the ${PAYABLE} Slot";

    public static String getUnsupportedPayableTypeMessage(Payable payable) {
        return ValidationExceptionMessageHandler.UNSUPPORTED_PAYABLE_TYPE_MESSAGE
                .replace("${PAYABLE_TYPE}", payable.getWorth().toString())
                .replace("${PAYABLE}", payable.getClass().getSimpleName());
    }

    public static String getInvalidPayableEnteredMessage(Payable payable) {
        return ValidationExceptionMessageHandler.INVALID_PAYABLE_ENTERED_MESSAGE
                .replace("${PAYABLE}", payable.getClass().getSimpleName());
    }
}
