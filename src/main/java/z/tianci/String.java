package z.tianci;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ZhangTianci
 */
public class String {
    /**
     * 判断是否为空字符串
     * 无值 以及 绝对的空
     *
     * @param str 源字符串
     * @return 校验结果
     */
    public static boolean isNullOrEmpty(java.lang.String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 不同于java.lang.String#isEmpty()
     * 判断去除\s 后 , 是否包含其他字符
     * 相对的空
     *
     * @param str 源字符串
     * @return 校验结果
     */
    public static boolean isEmpty(java.lang.String str) {
        return isNullOrEmpty(str) || str.replaceAll("\\s", "").length() == 0;
    }

    /**
     * MD5加码
     *
     * @param str 源字符串
     * @return 32位加密结果
     */
    public static java.lang.String md5(java.lang.String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        byte[] digestBytes = digest.digest(bytes);
        StringBuilder result = new StringBuilder();
        for (byte digestByte : digestBytes) {
            int value = ((int) digestByte) & 0xff;
            if (value < 16) {
                result.append("0");
            }
            result.append(Integer.toHexString(value));
        }
        return result.toString();
    }
}
