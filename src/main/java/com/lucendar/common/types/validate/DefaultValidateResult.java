package com.lucendar.common.types.validate;

import com.lucendar.common.types.AbstractValidateResultReceiver;

import java.util.ArrayList;
import java.util.List;

public class DefaultValidateResult extends AbstractValidateResultReceiver {

    private final List<ValidateResultItem> result = new ArrayList<>();

    @Override
    protected void onError(String message) {
        ValidateResultItem item = new ValidateResultItem(ValidateResultItemType.ERROR, message);
        result.add(item);
    }

    @Override
    public void warn(String message) {
        ValidateResultItem item = new ValidateResultItem(ValidateResultItemType.WARN, message);
        result.add(item);
    }

    public List<ValidateResultItem> getResult() {
        return result;
    }
}
