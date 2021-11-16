package com.hanhang.app;

import com.hanhang.inter.IApplication;


/**
 * @author hanhang
 */
public class TestApplication1 implements IApplication {
    @Override
    public void init() {
        System.out.println("TestApplication1--》300");
    }

    @Override
    public void execute() {
        System.out.println("TestApplication1--》execute");
    }

    @Override
    public void destroy() {
        System.out.println("TestApplication1--》destroy");
    }
}
