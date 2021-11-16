package com.hanhang.listener;

import com.hanhang.manage.ApplicationManager;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;

/**
 * @author hanhang
 */
public class JarFileChangeListener implements FileListener {
    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
        String name = fileChangeEvent.getFileObject().getName().getBaseName().replace(".class","");

        ApplicationManager.getInstance().reloadApplication(name);
    }

    @Override
    public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {
        String name = fileChangeEvent.getFileObject().getName().getBaseName().replace(".class","");

        ApplicationManager.getInstance().reloadApplication(name);
    }

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
        String name = fileChangeEvent.getFileObject().getName().getBaseName().replace(".class","");

        ApplicationManager.getInstance().reloadApplication(name);

    }
}
