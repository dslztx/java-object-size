package sizeofagent;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author dslztx
 * @date 2016Äê09ÔÂ11ÈÕ
 */
public class InstrumentationSizeOfAgent implements SizeOfAgent {
    static Instrumentation inst;

    public static void premain(String str, Instrumentation inst) {
        InstrumentationSizeOfAgent.inst = inst;
    }

    // avoid computing more than once
    Map<Object, Object> visited = new IdentityHashMap<Object, Object>();

    // save object graph
    Stack<Object> stack = new Stack();

    /**
     * compute Java Object Size itself other than recursively
     * 
     * @param obj can not be null
     * @return
     */
    public long sizeOf(Object obj) {
        if (inst == null) {
            throw new RuntimeException("inst object is null,please use \"-javaagent\" command arg");
        } else {
            return inst.getObjectSize(obj);
        }
    }

    /**
     * compute Java Object Size recursively
     * 
     * @param obj can not be null
     * @return
     */
    private long sizeOfRecursive(Object obj) {
        if (visited.containsKey(obj)) {
            return 0;
        }

        Class clz = obj.getClass();
        if (clz.isArray()) {
            int arrayLen = Array.getLength(obj);
            Class elementClz = clz.getComponentType();
            if (arrayLen != 0 && !elementClz.isPrimitive()) {
                for (int index = 0; index < arrayLen; index++) {
                    Object target = Array.get(obj, index);
                    if (target != null) {
                        stack.push(target);
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
                    if (field.getType().isPrimitive()) {
                        continue;
                    }

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

                clz = clz.getSuperclass();
            }
        }

        visited.put(obj, null);
        return inst.getObjectSize(obj);
    }

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
