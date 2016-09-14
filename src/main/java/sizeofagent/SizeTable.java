package sizeofagent;

/**
 * @author dslztx
 * @date 2016Äê09ÔÂ11ÈÕ
 */
public class SizeTable {
    private int bits;

    public SizeTable(int bits) {
        this.bits = bits;
    }

    public int markWordByteSize() {
        if (bits == 64) {
            return 8;
        } else {
            return 4;
        }
    }

    public int classPointerByteSize() {
        if (bits == 64) {
            return 8;
        } else {
            return 4;
        }
    }

    public int arrayRecordByteSize() {
        return 4;
    }

    public int typeByteSize(Class clz) {
        if (clz == boolean.class || clz == byte.class) {
            return 1;
        } else if (clz == char.class || clz == short.class) {
            return 2;
        } else if (clz == int.class || clz == float.class) {
            return 4;
        } else if (clz == long.class || clz == double.class) {
            return 8;
        } else {
            if (bits == 64) {
                return 8;
            } else {
                return 4;
            }
        }
    }

    public int paddingByteSize(long curByteSize) {
        if (curByteSize % 8 == 0) {
            return 0;
        } else {
            return (int) (8 - (curByteSize % 8));
        }
    }
}
