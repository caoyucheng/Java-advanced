import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class MyClassLoader extends ClassLoader {
    /**
     * 重写父类方法，返回一个Class对象
     */
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        String classFilename = name + ".xlass";
        File classFile = new File(classFilename);
        if (classFile.exists()) {
            try (FileChannel fileChannel = new FileInputStream(classFile)
                    .getChannel();) {
                MappedByteBuffer mappedByteBuffer = fileChannel
                        .map(MapMode.READ_ONLY, 0, fileChannel.size());
                byte[] b = mappedByteBuffer.array();
                for (byte c : b) {
                    c=(byte) (c+255);
                }
                clazz = defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    public static void main(String[] args) throws Exception{
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> clazz = myClassLoader.loadClass("Hello");
        Method sayHello = clazz.getMethod("hello");
        sayHello.invoke(clazz.newInstance());
    }
}