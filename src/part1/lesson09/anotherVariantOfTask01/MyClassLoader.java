package part1.lesson09.anotherVariantOfTask01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * MyClassLoader.
 * Custom classloader.
 * @author Aydar_Safiullin
 */
public class MyClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (SomeClass.class.getName().equals(name)) {
            return findClass(name);
        }

        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (SomeClass.class.getName().equals(name)) {
            try {
                byte[] allBytes = Files.readAllBytes(Paths.get("SomeClass.class"));
                return defineClass(name, allBytes, 0, allBytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.findClass(name);
    }
}
