/*
 * Copyright (c) 2024 lucendar.com.
 * All rights reserved.
 */
package com.lucendar.common.types;

/**
 * 可验证对象。通常用于配置，由配置对象实现此接口。
 */
public interface Validatable {

    /**
     * 对未设置的值，如有默认值，则设置默认值，最后验证所有配置值。
     *
     * @param receiver 验证结果接收对象
     */
    void settleAndValidate(ValidateResultReceiver receiver);
}
