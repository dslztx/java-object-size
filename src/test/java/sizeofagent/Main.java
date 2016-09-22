package sizeofagent;

import com.javamex.classmexer.MemoryUtil;
import org.openjdk.jol.info.GraphLayout;
import sizeof.agent.SizeOfAgent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // 48
        MyObject a = new MyObject();

        // 72
        MyObject2 b = new MyObject2();

        // 816
        long[] c = new long[100];
        for (int i = 0; i < 100; i++) {
            c[i] = i;
        }

        // 5216
        MyObject[] d = new MyObject[100];
        for (int i = 0; i < 100; i++) {
            d[i] = new MyObject();
        }

        // 每次运行不一样
        Set<Object> set = new HashSet<Object>();
        set.add(a);
        set.add(b);
        set.add(c);
        set.add(d);
        set.addAll(Arrays.asList(StringGenerator.generate(100000)));

        long aSize = LayoutSizeCalculator.graphSizeOf(a);
        long bSize = LayoutSizeCalculator.graphSizeOf(b);
        long cSize = LayoutSizeCalculator.graphSizeOf(c);
        long dSize = LayoutSizeCalculator.graphSizeOf(d);
        long setSize = LayoutSizeCalculator.graphSizeOf(set);

        System.out.println("\nCur Project Layout Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);

        long aSize2 = AgentSizeCalculator.graphSizeOf(a);
        long bSize2 = AgentSizeCalculator.graphSizeOf(b);
        long cSize2 = AgentSizeCalculator.graphSizeOf(c);
        long dSize2 = AgentSizeCalculator.graphSizeOf(d);
        long setSize2 = AgentSizeCalculator.graphSizeOf(set);

        System.out.println("\nCur Project Agent Algorithm");
        System.out.println("" + aSize2 + "," + bSize2 + "," + cSize2 + "," + dSize2 + "," + setSize2);

        test1(a, b, c, d, set);

        test2(a, b, c, d, set);

        test3(a, b, c, d, set);

        test4(a, b, c, d, set);

        test5(a, b, c, d, set);
    }

    private static void test5(MyObject a, MyObject2 b, long[] c, MyObject[] d, Set<Object> set) {
        long aSize = MemoryUtil.deepMemoryUsageOf(a, MemoryUtil.VisibilityFilter.ALL);
        long bSize = MemoryUtil.deepMemoryUsageOf(b, MemoryUtil.VisibilityFilter.ALL);
        long cSize = MemoryUtil.deepMemoryUsageOf(c, MemoryUtil.VisibilityFilter.ALL);
        long dSize = MemoryUtil.deepMemoryUsageOf(d, MemoryUtil.VisibilityFilter.ALL);
        long setSize = MemoryUtil.deepMemoryUsageOf(set, MemoryUtil.VisibilityFilter.ALL);

        System.out.println("\nClassMexer.jar Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);
    }

    private static void test4(MyObject a, MyObject2 b, long[] c, MyObject[] d, Set<Object> set) {
        long aSize = SizeOfAgent.fullSizeOf(a);
        long bSize = SizeOfAgent.fullSizeOf(b);
        long cSize = SizeOfAgent.fullSizeOf(c);
        long dSize = SizeOfAgent.fullSizeOf(d);
        long setSize = SizeOfAgent.fullSizeOf(set);

        System.out.println("\nSizeOfAgent.jar Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);
    }

    private static void test3(MyObject a, MyObject2 b, long[] c, MyObject[] d, Set<Object> set) {
        long aSize = ObjectSizeCalculator.getObjectSize(a);
        long bSize = ObjectSizeCalculator.getObjectSize(b);
        long cSize = ObjectSizeCalculator.getObjectSize(c);
        long dSize = ObjectSizeCalculator.getObjectSize(d);
        long setSize = ObjectSizeCalculator.getObjectSize(set);

        System.out.println("\nObjectSizeCalculator Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);
    }

    private static void test2(MyObject a, MyObject2 b, long[] c, MyObject[] d, Set<Object> set) {
        MemoryCounter counter = new MemoryCounter();
        long aSize = counter.estimate(a);
        long bSize = counter.estimate(b);
        long cSize = counter.estimate(c);
        long dSize = counter.estimate(d);
        long setSize = counter.estimate(set);

        System.out.println("\nMemoryCounter Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);
    }

    private static void test1(MyObject a, MyObject2 b, long[] c, MyObject[] d, Set<Object> set) {
        long aSize = GraphLayout.parseInstance(a).totalSize();
        long bSize = GraphLayout.parseInstance(b).totalSize();
        long cSize = GraphLayout.parseInstance(c).totalSize();
        long dSize = GraphLayout.parseInstance(d).totalSize();
        long setSize = GraphLayout.parseInstance(set).totalSize();

        System.out.println("\njol.jar Algorithm");
        System.out.println("" + aSize + "," + bSize + "," + cSize + "," + dSize + "," + setSize);
    }
}


class MyObject {
    boolean a;

    byte b;

    char c;

    short d;

    int e;

    float f;

    long g;

    double h;

    Object i;
}


class MyObject2 extends MyParent {
    boolean a;

    byte b;

    char c;

    short d;

    int e;

    float f;

    long g;

    double h;

    Object i;
}


class MyParent {
    int a;
    int b;
    char c;

    Object j;
    Object k;
    Object l;
}
