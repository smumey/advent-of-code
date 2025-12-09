package me.sol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunAll {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Error: runDir argument is required");
            System.err.println("Usage: java RunAll <runDir>");
            System.exit(1);
        }
        var runDir = args[0];
        if (runDir == null || runDir.trim().isEmpty()) {
            System.err.println("Error: runDir cannot be empty");
            System.exit(1);
        }

        var loader = RunAll.class.getClassLoader();
        var packagePath = "me/sol/" + runDir;
        var resource = loader.getResource(packagePath);

        if (resource == null) {
            System.err.println("Error: Directory not found: " + packagePath);
            System.err.println("Make sure the directory exists in the classpath");
            System.exit(1);
        }

        Path dirPath;
        try {
            dirPath = Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            System.err.println("Error: Invalid URI for directory: " + packagePath);
            e.printStackTrace();
            System.exit(1);
            return;
        }

        if (!Files.exists(dirPath)) {
            throw new IllegalArgumentException("Error: Directory does not exist: " + dirPath);
        }

        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Error: path is not a directory: " + dirPath);
        }

        Files.list(dirPath)
                .sorted()
                .map(file -> file.getFileName().toString())
                .filter(fileName -> fileName.endsWith(".class") && !fileName.contains("$"))
                .map(fileName -> fileName.substring(0, fileName.length() - 6))
                .map(className -> "me.sol." + runDir + "." + className)
                .forEach(fullClassName -> {
                    try {
                        execClass(Class.forName(fullClassName));
                    } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException("Warning: Could not exec %s".formatted(fullClassName), e);
                    }
                });
    }

    private static void execClass(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        try {
            var mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(clazz.getSimpleName() + "has no main method");
        }
    }
}
