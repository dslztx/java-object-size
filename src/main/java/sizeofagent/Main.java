package sizeofagent;

/**
 * @author dslztx
 * @date 2016Äê09ÔÂ12ÈÕ
 */
public class Main {
    public static void main(String[] args) {
        // SizeOfAgent agent1 = SizeOfAgentFactory.generate(Option.INSTRUMENTATION);
        // SizeOfAgent agent2 = SizeOfAgentFactory.generate(Option.SIMPLE);
        //
        // MyObject obj1 = new MyObject();
        // obj1.i = new Object();
        //
        // System.out.println("agent1-agent2:" + "(" + agent1.sizeOf(obj1) + "," + agent2.sizeOf(obj1) + ")");
        // System.out.println("agent1-agent2:" + "(" + agent1.graphSizeOf(obj1) + "," + agent2.graphSizeOf(obj1) + ")");
        //
        // MyObject2 obj2 = new MyObject2();
        // obj2.i = new Object();
        // obj2.j = obj1;
        // obj2.k = obj1;
        // obj2.l = obj1;
        // System.out.println("agent1-agent2:" + "(" + agent1.sizeOf(obj2) + "," + agent2.sizeOf(obj2) + ")");
        // System.out.println("agent1-agent2:" + "(" + agent1.graphSizeOf(obj2) + "," + agent2.graphSizeOf(obj2) + ")");
        //
        // int[] a = new int[100];
        // for (int i = 0; i < 100; i++) {
        // a[i] = i;
        // }
        //
        // System.out.println("agent1-agent2:" + "(" + agent1.sizeOf(a) + "," + agent2.sizeOf(a) + ")");
        // System.out.println("agent1-agent2:" + "(" + agent1.graphSizeOf(a) + "," + agent2.graphSizeOf(a) + ")");
        //
        // MyObject[] b = new MyObject[100];
        // for (int i = 0; i < 100; i++) {
        // b[i] = new MyObject();
        // }
        //
        // System.out.println("agent1-agent2:" + "(" + agent1.sizeOf(b) + "," + agent2.sizeOf(b) + ")");
        // System.out.println("agent1-agent2:" + "(" + agent1.graphSizeOf(b) + "," + agent2.graphSizeOf(b) + ")");


        // long[] bb = new long[11];
//        MyObject3 obj = new MyObject3();
//        System.out.println(ObjectSizeCalculator.getObjectSize(obj));
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


class MyParent2 {
    int a;
}


class MyObject3 extends MyParent2 {
    int b;
    long c;
}
