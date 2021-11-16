package com.hanhang.app;

import com.hanhang.inter.IApplication;


/**
 * @author hanhang
 */
public class TestApplication2 implements IApplication {
    @Override
    public void init() {
        System.out.println("TestApplication2--》init");
    }

    @Override
    public void execute() {
        System.out.println("TestApplication2--》execute");
    }

    @Override
    public void destroy() {
        System.out.println("TestApplication2--》destroy");
    }
}
