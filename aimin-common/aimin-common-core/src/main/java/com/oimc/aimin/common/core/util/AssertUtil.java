package com.oimc.aimin.common.core.util;

import com.oimc.aimin.common.core.exception.BaseException;

public abstract class AssertUtil {

    public static void isNull( Object object, BaseException e) {
        if (object != null) {
            throw e;
        }
    }

}


