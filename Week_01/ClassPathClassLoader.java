import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * class loader that load class from classpath
 */
public class ClassPathClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes;
        try {
            bytes = readBytes(name);
        } catch (IOException e) {
            throw new ClassNotFoundException("Can't read class file", e);
        }
        return super.defineClass(name, decode(bytes), 0, bytes.length);
    }

    private byte[] readBytes(String name) throws IOException {
        // resolve binary class name to resource path
        String filePath = "/" + name.replace(".", "/").concat(".xlass");
        try (InputStream is = this.getClass().getResourceAsStream(filePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (is == null) {
                throw new IOException("Didn't find the resource");
            }
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            return bos.toByteArray();
        }
    }

    // decode the bytes to original bytes
    // originalByte = 255 - curByte
    private byte[] decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        ClassPathClassLoader loader = new ClassPathClassLoader();
        Class<?> clz = loader.loadClass("Hello");
        Object instance = clz.newInstance();
        Method helloMethod = clz.getDeclaredMethod("hello");
        Object result = helloMethod.invoke(instance);
    }
}
