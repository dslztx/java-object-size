package sizeofagent;

/**
 * @author dslztx
 * @date 2016��09��11��
 */
public interface SizeOfAgent {
    long sizeOf(Object obj);

    long graphSizeOf(Object obj);
}
