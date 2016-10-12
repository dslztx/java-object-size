package sizeofagent;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author dslztx
 * @date 2016Äê09ÔÂ21ÈÕ
 */
public class LayoutSizeCalculator {

    private static SizeTable sizeTable;

    private static final char[] CC = new char[] {'0', '0', '0', '0', '0', '0', '0', '1'};

    private static Map<Class, ClassInfo> cache = new HashMap<Class, ClassInfo>();

    static {
        // default setting is 32 bits JDK
        int bits = 32;

        try {
            // may be 32 or 64
            bits = Integer.valueOf(System.getProperty("sun.arch.data.model"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sizeTable = new SizeTable(bits);
    }

    /**
     * compute Java Object Size itself other than recursively
     * 
     * @param obj
     * @return
     */
    public static long sizeOf(Object obj) {
        if (obj == null) {
            return 0;
        }

        Class clz = obj.getClass();
        if (clz.isArray()) {
            int len = Array.getLength(obj);

            Class elementClz = clz.getComponentType();

            long totalExcludePadding = sizeTable.arrayHeader() + len * sizeTable.fieldType(elementClz);

            long padding = sizeTable.padding(totalExcludePadding);

            return totalExcludePadding + padding;
        } else {
            if (cache.get(clz) == null) {
                build(clz);
            }

            ClassInfo clzInfo = cache.get(clz);

            long totalExcludePadding =
                    sizeTable.markWord() + sizeTable.classPointer() + clzInfo.getAncestorSize() + clzInfo.getSelfSize();
            long padding = sizeTable.padding(totalExcludePadding);

            return totalExcludePadding + padding;
        }
    }

    /**
     * compute Java Object Size recursively
     * 
     * @param obj
     * @return
     */
    public static long graphSizeOf(Object obj) {
        if (obj == null) {
            return 0;
        }

        Queue<Object> queue = new LinkedList<Object>();
        IdentitySet<Object> visited = new IdentitySet<Object>();
        long total = 0;

        queue.add(obj);
        while (!queue.isEmpty()) {
            total += internalSizeOf(queue.poll(), queue, visited);
        }

        return total;
    }

    private static ClassInfo build(Class clz) {
        if (cache.get(clz) != null) {
            return cache.get(clz);
        }

        if (clz == Object.class) {
            ClassInfo clzInfo = new ClassInfo();
            cache.put(clz, clzInfo);
            return clzInfo;
        }

        ClassInfo superClzInfo = build(clz.getSuperclass());

        ClassInfo clzInfo = new ClassInfo();
        long totalExcludePadding =
                superClzInfo.getAncestorSize() + superClzInfo.getExtra() + superClzInfo.getSelfSize();
        long padding = sizeTable.paddingSuperClass(totalExcludePadding);
        clzInfo.setAncestorSize(totalExcludePadding + padding);

        List<Field> fieldList = new LinkedList<Field>();
        long selfSize = 0;

        char[] c = new char[8];
        for (int index = 0; index < c.length; index++) {
            c[index] = '0';
        }
        int size;

        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            size = sizeTable.fieldType(field.getType());
            selfSize += size;

            c[size - 1] = '1';

            if (!field.getType().isPrimitive()) {
                field.setAccessible(true);
                fieldList.add(field);
            }
        }

        clzInfo.setSelfSize(selfSize);

        if (sizeTable.is32Bits() && (totalExcludePadding + padding) % 8 != 0 && Arrays.equals(c, CC)) {
            clzInfo.setExtra(4);
        }

        fieldList.addAll(superClzInfo.getFields());
        clzInfo.setFields(fieldList);

        cache.put(clz, clzInfo);

        return clzInfo;
    }



    private static long internalSizeOf(Object obj, Queue<Object> queue, IdentitySet<Object> visited) {
        if (visited.contains(obj)) {
            return 0;
        }

        Class clz = obj.getClass();

        long totalExcludePadding = 0;
        long padding = 0;

        if (clz.isArray()) {
            int len = Array.getLength(obj);

            Class elementClz = clz.getComponentType();

            if (len != 0 && !elementClz.isPrimitive()) {
                for (int index = 0; index < len; index++) {
                    Object target = Array.get(obj, index);
                    if (target != null) {
                        queue.add(target);
                    }
                }
            }

            totalExcludePadding = sizeTable.arrayHeader() + len * sizeTable.fieldType(elementClz);

            padding = sizeTable.padding(totalExcludePadding);
        } else {
            if (cache.get(clz) == null) {
                build(clz);
            }

            ClassInfo clzInfo = cache.get(clz);

            for (Field field : clzInfo.getFields()) {
                try {
                    Object target = field.get(obj);
                    if (target != null) {
                        queue.add(target);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            totalExcludePadding =
                    sizeTable.markWord() + sizeTable.classPointer() + clzInfo.getAncestorSize() + clzInfo.getSelfSize();
            padding = sizeTable.padding(totalExcludePadding);
        }

        visited.add(obj);

        return totalExcludePadding + padding;
    }
}


/**
 * compare reference,not content
 * 
 * @param <E>
 */
class IdentitySet<E> {
    IdentityHashMap<E, Object> map = new IdentityHashMap<E, Object>();

    public void add(E obj) {
        map.put(obj, null);
    }

    public boolean contains(E obj) {
        return map.containsKey(obj);
    }
}


class ClassInfo {
    /**
     * the size after align
     */
    long ancestorSize = 0;

    long selfSize = 0;

    long extra = 0;

    List<Field> fields = new LinkedList<Field>();

    public long getAncestorSize() {
        return ancestorSize;
    }

    public void setAncestorSize(long ancestorSize) {
        this.ancestorSize = ancestorSize;
    }

    public long getSelfSize() {
        return selfSize;
    }

    public void setSelfSize(long selfSize) {
        this.selfSize = selfSize;
    }

    public long getExtra() {
        return extra;
    }

    public void setExtra(long extra) {
        this.extra = extra;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
