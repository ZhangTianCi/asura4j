package z.tianci;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhangTianci
 */
@Slf4j
public class Number {
    /**
     * parseInt方法的拓展
     *
     * @param str 源字符串
     * @return 若转换成功, 返回数值. 否则返回null
     */
    public Integer tryParseInt(java.lang.String str) {
        try {
            if (String.isNullOrEmpty(str)) {
                log.error("整数转换失败,入参为空");
                return null;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            log.error("整数转换失败,入参不能转换为有效的整数");
            return null;
        }
    }

    /**
     * parseFloat方法的拓展
     *
     * @param str 源字符串
     * @return 若转换成功, 返回数值. 否则返回null
     */
    public Float tryParseFloat(java.lang.String str) {
        try {
            if (String.isNullOrEmpty(str)) {
                log.error("浮点值转换失败,入参为空");
                return null;
            }
            return Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            log.error("浮点值转换失败,入参不能转换为有效的整数");
            return null;
        }
    }

    /**
     * parseDouble方法的拓展
     *
     * @param str 源字符串
     * @return 若转换成功, 返回数值. 否则返回null
     */
    public Double tryParseDouble(java.lang.String str) {
        try {
            if (String.isNullOrEmpty(str)) {
                log.error("浮点值转换失败,入参为空");
                return null;
            }
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            log.error("浮点值转换失败,入参不能转换为有效的整数");
            return null;
        }
    }
}
