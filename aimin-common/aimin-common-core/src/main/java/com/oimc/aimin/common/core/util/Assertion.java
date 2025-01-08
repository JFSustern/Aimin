package com.oimc.aimin.common.core.util;

import com.oimc.aimin.common.core.exception.CommonErrorType;
import com.oimc.aimin.common.core.exception.CommonException;

public abstract class Assertion {

    public static void notNull( Object object) {
        if (object == null) {
            throw new CommonException(CommonErrorType.OBJECT_NOT_NULL);
        }
    }

}


