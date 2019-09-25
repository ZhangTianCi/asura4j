package z.tianci;


/**
 * @author ZhangTianci
 */
public class Map {

    /**
     * 不同于java.util.Map#containsKey(java.lang.Object)
     * 不仅确认key存在,且判断key对应的值是否为空
     *
     * @param map 键值对
     * @param key 键
     * @return
     */
    public boolean containsKey(java.util.Map map, Object key) {
        return map.containsKey(key) && map.get(key) != null;
    }
}
