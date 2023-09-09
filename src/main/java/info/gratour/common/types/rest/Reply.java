package info.gratour.common.types.rest;

import com.google.gson.reflect.TypeToken;
import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Reply<T> {

    public static interface Mapper<T, C> {

        C map(T entry);
    }

    public static final Type RAW_REPLY_TYPE = new TypeToken<Reply<Void>>(){}.getType();

    private int errCode;
    private String message;
    private T[] data;
    private Long count;

    public Reply() {
    }

    public Reply(int errCode) {
        this(errCode, Errors.errorMessage(errCode));
    }

    public Reply(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;

    }

    public Reply(T[] data, Long count) {
        this.data = data;
        this.count = count;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

//    public boolean isOk() {
//        return errCode == Errors.OK;
//    }

    public boolean hasData() {
        return data != null && data.length > 0;
    }

    /**
     *
     * @return a non-empty list
     */
    public List<T> dataList() {
        if (data != null)
            return Arrays.asList(data);
        else
            return new ArrayList<>();
    }

    public T first() {
        if (!hasData())
            throw new ErrorWithCode(Errors.INTERNAL_ERROR, "Reply has no data.");
        return data[0];
    }

    public T firstOrNull() {
        if (hasData())
            return data[0];
        else
            return null;
    }

    @SuppressWarnings({"unchecked"})
    public <C> Reply<C> map(Mapper<T, C> mapper) {
        if (hasData()) {
            C[] arr = (C[]) new Object[data.length];
            for (int i = 0; i < data.length; i++) {
                C c = mapper.map(data[i]);
                arr[i] = c;
            }

            return new Reply<C>(arr, count);
        } else
            return new Reply<>((C[]) new Object[0], count);
    }

    public void forEach(Consumer<T> consumer) {
        if (hasData() && consumer != null) {
            for (T datum : data)
                consumer.accept(datum);
        }
    }

    public ErrorWithCode toErrorWithCode() {
        return new ErrorWithCode(errCode, message);
    }

    public boolean ok() {
        return errCode == Errors.OK;
    }

    public boolean isEmpty() {
        return data == null || data.length == 0;
    }

    public boolean nonEmpty() {
        return !isEmpty();
    }

    public Reply<Void> toVoid() {
        return new Reply<>(errCode, message);
    }

    public static Reply<?> ofUpdateResult(boolean recordUpdated) {
        if (recordUpdated)
            return OK;
        else
            return RECORD_NOT_FOUND;
    }

    public static Reply<?> ofUpdateResult(int updateRecordCount) {
        return ofUpdateResult(updateRecordCount > 0);
    }

    public static <T> Reply<T> single(T data) {
        if (data == null)
            return new Reply<T>(Errors.RECORD_NOT_FOUND);

        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[]{data};
        Reply<T> r = new Reply<T>(Errors.OK);
        r.setData(arr);
        r.setCount(1L);

        return r;
    }

    public static <T> Reply<T> multi(T[] data, long totalRecordCount) {
        Reply<T> r = new Reply<T>(Errors.OK);
        r.setData(data);
        r.setCount(totalRecordCount);

        return r;
    }

    public static <T> Reply<T> multi(T[] data) {
        return multi(data, data.length);
    }

    public static <T> Reply<T> multi(List<T> data, long totalRecordCount) {
        Reply<T> r = new Reply<T>(Errors.OK);
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[data.size()];
        data.toArray(arr);

        r.setData(arr);
        r.setCount(totalRecordCount);

        return r;
    }

    public static <T> Reply<T> multi(List<T> data) {
        return multi(data, data.size());
    }

    public static <T> Reply<T> empty() {
        Reply<T> r = new Reply<T>(Errors.OK);
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[0];
        r.setData(arr);
        return r;
    }

    public static <T> Reply<T> error(int errCode) {
        return error(errCode, Errors.errorMessage(errCode));
    }

    public static <T> Reply<T> error(int errCode, String message) {
        return new Reply<T>(errCode, message);
    }

    public static <T> Reply<T> error(ErrorWithCode e) {
        return error(e.getErrCode(), e.getMessage());
    }

    public static <T> Reply<T> error(Reply<?> r) {
        return new Reply<>(r.getErrCode(), r.getMessage());
    }

    public static <T> Reply<T> invalidParam(String paramName) {
        return error(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Reply.class.getSimpleName() + "[", "]")
                .add("errCode=" + errCode)
                .add("message='" + message + "'")
                .add("data=" + Arrays.toString(data))
                .add("count=" + count)
                .toString();
    }


    public static final Reply<?> OK = new Reply<>(Errors.OK);
    public static final Reply<Void> OK_VOID = new Reply<>(Errors.OK);

    public static final Reply<?> INTERNAL_ERROR = new Reply<>(Errors.INTERNAL_ERROR);

    public static <T> Reply<T> internalError() {
        return new Reply<>(Errors.INTERNAL_ERROR);
    }

    public static final Reply<?> AUTHENTICATION_FAILED = new Reply<>(Errors.AUTHENTICATION_FAILED);
    public static <T> Reply<T> authenticationFailed() {
        return new Reply<>(Errors.AUTHENTICATION_FAILED);
    }

    public static final Reply<?> INVALID_TOKEN = new Reply<>(Errors.INVALID_TOKEN);
    public static <T> Reply<T> invalidToken() {
        return new Reply<>(Errors.INVALID_TOKEN);
    }

    public static final Reply<?> BAD_FORMAT = new Reply<>(Errors.BAD_FORMAT);
    public static <T> Reply<T> badFormat() {
        return new Reply<>(Errors.BAD_FORMAT);
    }

    public static final Reply<?> DUPLICATED_VALUE = new Reply<>(Errors.DUPLICATED_VALUE);
    public static <T> Reply<T> duplicatedValue() {
        return new Reply<>(Errors.DUPLICATED_VALUE);
    }

    public static final Reply<?> RECORD_NOT_FOUND = new Reply<>(Errors.RECORD_NOT_FOUND);
    public static <T> Reply<T> recordNotFound() {
        return new Reply<>(Errors.RECORD_NOT_FOUND);
    }

    public static final Reply<?> ACCESS_DENIED = new Reply<>(Errors.ACCESS_DENIED);
    public static <T> Reply<T> accessDenied() {
        return new Reply<>(Errors.ACCESS_DENIED);
    }

    public static final Reply<?> SESSION_EXPIRED = new Reply<>(Errors.SESSION_EXPIRED);
    public static <T> Reply<T> sessionExpired() {
        return new Reply<>(Errors.SESSION_EXPIRED);
    }

    public static final Reply<?> HTTP_METHOD_NOT_SUPPORT = new Reply<>(Errors.HTTP_METHOD_NOT_SUPPORT);
    public static <T> Reply<T> httpMethodNotSupport() {
        return new Reply<>(Errors.HTTP_METHOD_NOT_SUPPORT);
    }

    public static final Reply<?> MISSING_REQUEST_PARAM = new Reply<>(Errors.MISSING_REQUEST_PARAM);
    public static <T> Reply<T> missingRequestParam() {
        return new Reply<>(Errors.MISSING_REQUEST_PARAM);
    }

    public static final Reply<?> BAD_REQUEST = new Reply<>(Errors.BAD_REQUEST);
    public static <T> Reply<T> badRequest() {
        return new Reply<>(Errors.BAD_REQUEST);
    }

    public static final Reply<?> INVALID_CONFIG = new Reply<>(Errors.INVALID_CONFIG);
    public static <T> Reply<T> invalidConfig() {
        return new Reply<>(Errors.INVALID_CONFIG);
    }

    public static final Reply<?> TIMEOUT = new Reply<>(Errors.TIMEOUT);
    public static <T> Reply<T> timeout() {
        return new Reply<>(Errors.TIMEOUT);
    }

    public static final Reply<?> ILLEGAL_STATE = new Reply<>(Errors.ILLEGAL_STATE);
    public static <T> Reply<T> illegalState() {
        return new Reply<>(Errors.ILLEGAL_STATE);
    }

    public static final Reply<?> EXECUTION_ERROR = new Reply<>(Errors.EXECUTION_ERROR);
    public static <T> Reply<T> executionError() {
        return new Reply<>(Errors.EXECUTION_ERROR);
    }

    public static final Reply<?> SERVICE_UNAVAILABLE = new Reply<>((Errors.SERVICE_UNAVAILABLE));
    public static <T> Reply<T> serviceUnavailable() {
        return new Reply<>((Errors.SERVICE_UNAVAILABLE));
    }

}
