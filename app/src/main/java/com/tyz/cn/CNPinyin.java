package com.tyz.cn;

import java.io.Serializable;

import static com.tyz.cn.CNPinyinFactory.DEF_CHAR;

/**
 * Created by com on 2017/9/7.
 */

public class CNPinyin <T extends CN> implements Serializable, Comparable<CNPinyin<T>> {

    /**
     * 对应首字首拼音字母
     */
    char firstChar;
    /**
     * 所有字符中的拼音首字母
     */
    String firstChars;
    /**
     * 对应的所有字母拼音
     */
    String[] pinyins;

    /**
     * 拼音总长度
     */
    int pinyinsTotalLength;

    public final T data;

    CNPinyin(T data) {
        this.data = data;
    }

    public char getFirstChar() {
        return firstChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("--firstChar--").append(firstChar).append("--pinyins:");
        for (String str : pinyins) {
            sb.append(str);
        }
        return sb.toString();
    }

    int compareValue() {
        if (firstChar == DEF_CHAR) {
            return 'Z' + 1;
        }
        return firstChar;
    }

    @Override
    public int compareTo(CNPinyin<T> tcnPinyin) {
        int compare = compareValue() - tcnPinyin.compareValue();
        if (compare == 0) {
            String chinese1 = data.chinese();
            String chinese2 = tcnPinyin.data.chinese();
            return chinese1.compareTo(chinese2);
        }
        return compare;
    }
}
