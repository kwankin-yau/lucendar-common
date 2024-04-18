/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package com.lucendar.common.types;

/**
 * 验证结果接收器基类
 */
abstract public class AbstractValidateResultReceiver implements ValidateResultReceiver {

    /**
     * 是否发生的错误
     */
    protected boolean err;

    @Override
    public boolean ok() {
        return !err;
    }

    /**
     * 发生错误时的处理。由子类实现
     * @param message 错误信息
     */
    protected abstract void onError(String message);

    @Override
    public void error(String message) {
        err = true;
        onError(message);
    }
}
