package com.hanhang.inter.impl;

import com.hanhang.inter.IClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hanhang
 */
public class SimpleJarLoader implements IClassLoader {
    @Override
    public ClassLoader createClassLoader(ClassLoader parentClassLoader, String... paths) {
        List<URL> jarsToLoad = new ArrayList<>();
        for (String folder : paths) {
            List<String> jarPaths = scanJarFiles(folder);

            for (String jar : jarPaths) {

                try {
                    File file = new File(jar);
                    jarsToLoad.add(file.toURI().toURL());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        URL[] urls = new URL[jarsToLoad.size()];
        jarsToLoad.toArray(urls);

        return new URLClassLoader(urls, parentClassLoader);
    }

    /**
     * 扫描文件
     * @param folderPath 文件路径
     * @return 文件列表
     */
    private List<String> scanJarFiles(String folderPath) {

        List<String> jars = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new RuntimeException("扫描的路径不存在, path:" + folderPath);
        }

        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (!f.isFile()) {
                continue;
            }
            String name = f.getName();

            if (name.length() == 0) {
                continue;
            }

            int extIndex = name.lastIndexOf(".");
            if (extIndex < 0) {
                continue;
            }

            String ext = name.substring(extIndex);
            if (!".jar".equalsIgnoreCase(ext)) {
                continue;
            }

            jars.add(folderPath + "/" + name);
        }
        return jars;
    }
}
