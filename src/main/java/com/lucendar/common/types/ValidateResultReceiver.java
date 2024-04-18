/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package com.lucendar.common.types;

import info.gratour.common.error.Errors;

/**
 * 验证结果接收器
 */
public interface ValidateResultReceiver {

    /**
     * 返回验证是否通过
     *
     * @return 验证是否通过
     */
    boolean ok();

    /**
     * 返回验证过程是否发生的错误
     *
     * @return 验证过程是否发生的错误
     */
    default boolean error() {
        return !ok();
    }

    /**
     * 反馈错误
     *
     * @param message 错误信息
     */
    void error(String message);

    /**
     * 反馈错误：无效的字段（属性）
     *
     * @param fieldName 无效的字段（属性）
     */
    default void invalidField(String fieldName) {
        String msg = Errors.errorMessageFormat(Errors.INVALID_CONFIG, fieldName);
        error(msg);
    }

    /**
     * 反馈警告信息
     *
     * @param message 警告信息
     */
    void warn(String message);
}
