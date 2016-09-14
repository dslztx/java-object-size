package sizeofagent;

/**
 * @author dslztx
 * @date 2016年09月11日
 */
public class SizeOfAgentFactory {
    // 这里又可以搞个单例
    // SizeOfAgent agent;

    public static SizeOfAgent generate(Option option) {
        if (option == Option.INSTRUMENTATION) {
            return new InstrumentationSizeOfAgent();
        } else {
            return new SimpleSizeOfAgent();
        }
    }
}
