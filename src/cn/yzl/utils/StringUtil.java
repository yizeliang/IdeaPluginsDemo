package cn.yzl.utils;

import org.apache.http.util.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YZL on 2017/4/25.
 */
public class StringUtil {
    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 根据驼峰写法将单词分割开来
     *
     * @return
     */
    public static String getWords(String en) {
        if (isContainChinese(en)) {
            return en;
        }
        if (TextUtils.isEmpty(en)) {
            return en;
        }
        StringBuffer sb = new StringBuffer();

        char[] chars = en.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                sb.append(chars[i]);
                continue;
            }
            if (Character.isUpperCase(chars[i])) {
                //是大写
                sb.append(" ");
            }
            sb.append(chars[i]);
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
