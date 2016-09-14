package sizeofagent;

/**
 * @author dslztx
 * @date 2016��09��11��
 */
public class SizeOfAgentFactory {
    // �����ֿ��Ը������
    // SizeOfAgent agent;

    public static SizeOfAgent generate(Option option) {
        if (option == Option.INSTRUMENTATION) {
            return new InstrumentationSizeOfAgent();
        } else {
            return new SimpleSizeOfAgent();
        }
    }
}
