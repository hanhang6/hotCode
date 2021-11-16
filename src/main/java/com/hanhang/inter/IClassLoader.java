package com.hanhang.inter;

/**
 * @author hanhang
 */
public interface IClassLoader {
    /**
     * 创建classLoader
     * @param parentClassLoader 父classLoader
     * @param paths 路径
     * @return 类加载器
     */
    ClassLoader createClassLoader(ClassLoader parentClassLoader, String...paths);
}
