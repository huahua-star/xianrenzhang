package org.jeecg.modules.zzj.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:59:27
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(500, "错误异常，请联系后台");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }
    public static R ok(String key,Object value) {
        R r = new R();
        r.put(key, value);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    /**判断参数r是否是正确的返回信息
     * <p>code的值为0时返回true，否则返回false。
     * @param r
     * @return
     */
    public static boolean isOk(R r) {
        try {
            return 0==(int)r.get("code");
        }catch (Exception e) {
            return false;
        }
    }
    /**判断参数r是否包含错误
     * <p>code的值不为0时，代表包含错误信息，返回true。否则返回false
     * @param r
     * @return
     */
    public static boolean isError(R r) {
        return !isOk(r);
    }
}
