package win.arora.vishal.edf.utils;

import win.arora.vishal.edf.exception.EDFException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class EDFInputStream extends InputStream {

    private InputStream is;

    public EDFInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    public Short readShort() throws IOException {
        //Short is 2 bytes, so read 2 bytes first
        byte[] bytes = readNBytes(2);
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort();
    }

    public List<byte[]> readMultiByteArray(int size, int length) throws IOException {
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(readNBytes(size));
        }
        return result;
    }

    public List<String> readMultiASCII(int size, int length) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(readASCII(size));
        }
        return result;
    }

    public List<Double> readMultiDouble(int size, int length) throws IOException {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(readDouble(size));
        }
        return result;
    }

    public List<Integer> readMultiInt(int size, int length) throws IOException {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(readInt(size));
        }
        return result;
    }

    public Double readDouble(int size) throws IOException {
        return Double.parseDouble(readASCII(size));
    }

    public Integer readInt(int size) throws IOException {
        return Integer.parseInt(readASCII(size));
    }

    public String readASCII(int size) throws IOException {
        int len;
        byte[] data = new byte[size];
        len = is.read(data);
        if (len != data.length) {
            throw new EDFException("Could need read data");
        }
        return new String(data, Constants.CHARSET).trim();
    }
}
