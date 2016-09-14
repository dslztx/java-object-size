package sizeofagent;

/**
 * @date 2016��09��12��
 */
public class Main2 {
    public static void main(String[] args) {
        SizeOfAgent agent1 = SizeOfAgentFactory.generate(Option.INSTRUMENTATION);
        SizeOfAgent agent2 = SizeOfAgentFactory.generate(Option.SIMPLE);

        String a = new String("hello");

        System.out.println(agent1.sizeOf(a));
        System.out.println(agent2.sizeOf(a));

        System.out.println(agent1.graphSizeOf(a));
        System.out.println(agent2.graphSizeOf(a));
    }
}
