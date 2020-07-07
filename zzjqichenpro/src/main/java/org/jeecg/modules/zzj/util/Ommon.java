package org.jeecg.modules.zzj.util;

import static org.springframework.util.StringUtils.isEmpty;

public class Ommon {

    /**
     * JAVA从指定位置开始截取指定长度的字符串
     *
     * @param input 输入字符串
     * @param index 截取位置，左侧第一个字符索引值是1
     * @param count 截取长度
     * @return 截取字符串
     */
    public static String middle(String input, int index, int count) {
        if (isEmpty(input)) {
            return "";
        }
        count = (count > input.length() - index + 1) ? input.length() - index + 1 :
                count;
        return input.substring(index - 1, index + count - 1);
    }


}
