/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package com.lucendar.common.types;

abstract public class AbstractValidateResultReceiver implements ValidateResultReceiver {

    protected boolean err;

    @Override
    public boolean ok() {
        return !err;
    }

}
