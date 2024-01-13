/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package com.lucendar.common.types;

/**
 * Validate result receiver
 */
public interface ValidateResultReceiver {

    /**
     * Whether validation success
     *
     * @return whether validation success
     */
    boolean ok();
    default boolean error() {
        return !ok();
    }

    void invalidField(String fieldName);
    void error(String message);
}
