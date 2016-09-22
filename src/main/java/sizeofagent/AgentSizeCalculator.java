package sizeofagent;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author dslztx
 * @date 2016Äê09ÔÂ11ÈÕ
 */
public class AgentSizeCalculator {
    static Instrumentation inst;

    public static void premain(String str, Instrumentation inst) {
        AgentSizeCalculator.inst = inst;
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

        if (inst == null) {
            throw new RuntimeException("inst object is null,please use \"-javaagent\" command arg");
        } else {
            return inst.getObjectSize(obj);
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

        if (inst == null) {
            throw new RuntimeException("inst object is null,please use \"-javaagent\" command arg");
        } else {
            Queue<Object> queue = new LinkedList<Object>();
            IdentitySet<Object> visited = new IdentitySet<Object>();

            long total = 0;

            queue.add(obj);
            while (!queue.isEmpty()) {
                total += internSizeOf(queue.poll(), queue, visited);
            }

            return total;
        }
    }

    private static long internSizeOf(Object obj, Queue<Object> queue, IdentitySet<Object> visited) {
        if (visited.contains(obj)) {
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
                        queue.add(target);
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
                            queue.add(target);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                clz = clz.getSuperclass();
            }
        }

        visited.add(obj);

        return inst.getObjectSize(obj);
    }
}
