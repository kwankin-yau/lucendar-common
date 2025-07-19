/**
 * ****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 * <p>
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 ******************************************************************************/
package info.gratour.common.error;

public class ErrorWithCode extends RuntimeException {

    private final int errCode;

    public ErrorWithCode(int errCode) {
        this(errCode, Errors.errorMessage(errCode), null);
    }

    public ErrorWithCode(int errCode, String message) {
        this(errCode, message, null);

    }

    public ErrorWithCode(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public ErrorWithCode(int errCode, Throwable cause) {
        super(Errors.errorMessage(errCode), cause);
        this.errCode = errCode;
    }

    public ErrorWithCode(int errCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public boolean ok() {
        return errCode == Errors.OK;
    }

    public static ErrorWithCode OK = new ErrorWithCode(Errors.OK);

    public static ErrorWithCode internalError() {
        return new ErrorWithCode(Errors.INTERNAL_ERROR);
    }

    public static ErrorWithCode internalError(String message) {
        message = Errors.errorMessage(Errors.INTERNAL_ERROR) + " " + message;
        return new ErrorWithCode(Errors.INTERNAL_ERROR, message);
    }

    public static ErrorWithCode internalErrorFmt(String fmt, Object... args) {
        return internalError(String.format(fmt, args));
    }


    public static ErrorWithCode authenticationFailed() {
        return new ErrorWithCode(Errors.AUTHENTICATION_FAILED);
    }

    public static ErrorWithCode notAuthenticated() {
        return new ErrorWithCode(Errors.NOT_AUTHENTICATED);
    }

    public static ErrorWithCode accessDenied() {
        return new ErrorWithCode(Errors.ACCESS_DENIED);
    }

    public static ErrorWithCode notEnoughPriv() {
        return new ErrorWithCode(Errors.NOT_ENOUGH_PRIV);
    }

    public static ErrorWithCode recordNotFound() {
        return new ErrorWithCode(Errors.RECORD_NOT_FOUND);
    }

    public static ErrorWithCode recordNotFound(String rec) {
        return new ErrorWithCode(Errors.RECORD_NOT_FOUND,
                Errors.errorMessageFormat(Errors.MESSAGE_KEY_RECORD_NOT_FOUND_FMT, rec)
        );
    }

    public static ErrorWithCode recordNotFoundFmt(String fmt, Object... args) {
        return recordNotFound(String.format(fmt, args));
    }

    public static ErrorWithCode executionError() {
        return new ErrorWithCode(Errors.EXECUTION_ERROR);
    }

    public static ErrorWithCode executionError(String detail) {
        return new ErrorWithCode(
                Errors.EXECUTION_ERROR,
                Errors.errorMessageFormat(Errors.MESSAGE_KEY_EXECUTION_ERROR_FMT, detail)
        );
    }

    public static ErrorWithCode executionErrorFmt(String fmt, Object... args) {
        return executionError(String.format(fmt, args));
    }

    public static ErrorWithCode illegalState() {
        return new ErrorWithCode(Errors.ILLEGAL_STATE);
    }

    public static ErrorWithCode illegalState(String explanation) {
        return new ErrorWithCode(
                Errors.ILLEGAL_STATE,
                Errors.errorMessageFormat(Errors.MESSAGE_KEY_ILLEGAL_STATE_FMT, explanation)
        );
    }

    public static ErrorWithCode illegalStateFmt(String fmt, Object...args) {
        return illegalState(String.format(fmt, args));
    }

    public static ErrorWithCode invalidConfig() {
        return new ErrorWithCode(Errors.INVALID_CONFIG);
    }

    public static ErrorWithCode invalidConfig(String configItem) {
        return new ErrorWithCode(
                Errors.INVALID_CONFIG,
                Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_CONFIG_FMT, configItem)
        );
    }

    public static ErrorWithCode invalidConfigFmt(String fmt, Object...args) {
        return new ErrorWithCode(Errors.INVALID_CONFIG, String.format(fmt, args));
    }

    public static ErrorWithCode timeout() {
        return new ErrorWithCode(Errors.TIMEOUT);
    }

    public static ErrorWithCode serviceUnavailable() {
        return new ErrorWithCode(Errors.SERVICE_UNAVAILABLE);
    }

    public static ErrorWithCode canceled() {
        return new ErrorWithCode(Errors.CANCELED);
    }

    public static ErrorWithCode invalidParam(String paramName) {
        return new ErrorWithCode(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName));
    }

    public static ErrorWithCode invalidParam(String paramName, String reason) {
        String message = Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName) + " " + reason;
        return new ErrorWithCode(Errors.INVALID_PARAM, message);
    }

    public static ErrorWithCode invalidValue(String fieldName) {
        return new ErrorWithCode(
                Errors.INVALID_VALUE,
                Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_VALUE_FMT, fieldName)
        );
    }

    public static ErrorWithCode notSupported() {
        return new ErrorWithCode(Errors.NOT_SUPPORTED);
    }

    public static ErrorWithCode format(int errCode, String formatArg) {
        String message = Errors.errorMessageFormat(errCode, formatArg);
        return new ErrorWithCode(errCode, message);
    }


}
