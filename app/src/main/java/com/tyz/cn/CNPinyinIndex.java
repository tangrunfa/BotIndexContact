package com.tyz.cn;

import java.io.Serializable;

/**
 * Created by com on 2017/9/8.
 */

public class CNPinyinIndex <T extends CN> implements Serializable {

    public final CNPinyin<T> cnPinyin;

    public final int start;

    public final int end;

    CNPinyinIndex(CNPinyin cnPinyin, int start, int end) {
        this.cnPinyin = cnPinyin;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return cnPinyin.toString()+"  start " + start+"  end " + end;
    }
}
