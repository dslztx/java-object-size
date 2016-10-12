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

    public int markWord() {
        if (bits == 64) {
            return 8;
        } else {
            return 4;
        }
    }

    public int classPointer() {
        if (bits == 64) {
            return 8;
        } else {
            return 4;
        }
    }

    public int arrayHeader() {
        if (bits == 64) {
            return (8 + 8 + 4) + 4;
        } else {
            return 4 + 4 + 4;
        }
    }

    public int fieldType(Class clz) {
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

    public int padding(long curByteSize) {
        if (curByteSize % 8 == 0) {
            return 0;
        } else {
            return (int) (8 - (curByteSize % 8));
        }
    }

    public int paddingSuperClass(long curByteSize) {
        int paddingLen = 8;
        if (bits == 32) {
            paddingLen = 4;
        }

        if (curByteSize % paddingLen == 0) {
            return 0;
        } else {
            return (int) (paddingLen - (curByteSize % paddingLen));
        }
    }

    public boolean is32Bits() {
        return bits == 32;
    }
}
