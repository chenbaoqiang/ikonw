package com.feinno.androidbase.utils.log;

import android.content.Context;

/**
 * Created by ganshoucong on 2015/12/12.
 */
public class RFAssert {
    static public void rfAssert(boolean b){
        if(!b)
            throw new RuntimeException();
    }
}
