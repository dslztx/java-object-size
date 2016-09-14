package sizeofagent;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * compute Java Object Size using Java Object Layout
 * 
 * @author dslztx
 * @date 2016Äê09ÔÂ11ÈÕ
 */
public class SimpleSizeOfAgent implements SizeOfAgent {

    // provide 'Byte Sizes' for different parts
    SizeTable sizeTable;

    // avoid computing more than once
    Map<Object, Object> visited = new IdentityHashMap<Object, Object>();

    // save object graph
    Stack<Object> stack = new Stack();

    public SimpleSizeOfAgent() {
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
     * @param obj can not be null
     * @return
     */
    public long sizeOf(Object obj) {
        long size = 0;

        size += sizeTable.markWordByteSize();
        size += sizeTable.classPointerByteSize();

        Class clz = null;

        clz = obj.getClass();

        if (clz.isArray()) {
            size += sizeTable.arrayRecordByteSize();

            int arrayLen = Array.getLength(obj);
            if (arrayLen != 0) {
                Class elementClz = clz.getComponentType();
                size += sizeTable.typeByteSize(elementClz) * arrayLen;
            }
        } else {
            while (clz != null) {
                Field[] fields = clz.getDeclaredFields();
                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    size += sizeTable.typeByteSize(field.getType());
                }

                clz = clz.getSuperclass();
            }
        }

        size += sizeTable.paddingByteSize(size);

        return size;
    }

    /**
     * 
     * @param obj can not be null
     * @return
     */
    private long sizeOfRecursive(Object obj) {
        if (visited.containsKey(obj)) {
            return 0;
        }

        long size = 0;

        size += sizeTable.markWordByteSize();
        size += sizeTable.classPointerByteSize();

        Class clz = null;
        clz = obj.getClass();

        if (clz.isArray()) {
            size += sizeTable.arrayRecordByteSize();

            int arrayLen = Array.getLength(obj);
            if (arrayLen != 0) {
                Class elementClz = clz.getComponentType();
                size += sizeTable.typeByteSize(elementClz) * arrayLen;

                if (!elementClz.isPrimitive()) {
                    for (int index = 0; index < arrayLen; index++) {
                        Object target = Array.get(obj, index);
                        if (target != null) {
                            stack.push(target);
                        }
                    }
                }
            }
        } else {
            while (clz != null) {
                Field[] fields = clz.getDeclaredFields();
                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    size += sizeTable.typeByteSize(field.getType());

                    if (!field.getType().isPrimitive()) {
                        try {
                            field.setAccessible(true);
                            Object target = field.get(obj);
                            if (target != null) {
                                stack.push(target);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                clz = clz.getSuperclass();
            }
        }

        size += sizeTable.paddingByteSize(size);

        visited.put(obj, null);

        return size;
    }

    /**
     * compute Java Object Size recursively
     * 
     * @param obj
     * @return
     */
    public long graphSizeOf(Object obj) {
        long size = 0;

        stack.push(obj);
        while (!stack.isEmpty()) {
            size += sizeOfRecursive(stack.pop());
        }
        visited.clear();

        return size;
    }
}
