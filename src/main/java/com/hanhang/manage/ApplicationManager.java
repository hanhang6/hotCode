package com.hanhang.manage;

import com.hanhang.config.AppConfigList;
import com.hanhang.config.GlobalSetting;
import com.hanhang.inter.IApplication;
import com.hanhang.inter.IClassLoader;
import com.hanhang.inter.impl.SimpleJarLoader;
import com.hanhang.listener.JarFileChangeListener;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hanhang
 */
public class ApplicationManager {
    private static ApplicationManager instance;

    private IClassLoader jarLoader;
    private AppConfigManager configManager;

    private Map<String, IApplication> apps;

    private ApplicationManager(){
    }

    public void init(){
        jarLoader = new SimpleJarLoader();
        configManager = new AppConfigManager();
        apps = new HashMap<>();

        initAppConfigs();

        URL basePath = this.getClass().getClassLoader().getResource("");

        loadAllApplications(Objects.requireNonNull(basePath).getPath());

        initMonitorForChange(basePath.getPath());
    }

    /**
     * 初始化配置
     */
    public void initAppConfigs(){

        try {
            URL path = this.getClass().getClassLoader().getResource(GlobalSetting.APP_CONFIG_NAME);
            configManager.loadAllApplicationConfigs(Objects.requireNonNull(path).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载类
     * @param basePath 根目录
     */
    public void loadAllApplications(String basePath){

        for(AppConfigList.AppConfig config : this.configManager.getConfigs()){
            this.createApplication(basePath, config);
        }
    }

    /**
     * 初始化监听器
     * @param basePath 路径
     */
    public void initMonitorForChange(String basePath){
        try {
            FileSystemManager fileManager = VFS.getManager();

            File file = new File(basePath + GlobalSetting.JAR_FOLDER);
            FileObject monitoredDir = fileManager.resolveFile(file.getAbsolutePath());
            FileListener fileMonitorListener = new JarFileChangeListener();
            DefaultFileMonitor fileMonitor = new DefaultFileMonitor(fileMonitorListener);
            fileMonitor.setRecursive(true);
            fileMonitor.addFile(monitoredDir);
            fileMonitor.start();
            System.out.println("Now to listen " + monitoredDir.getName().getPath());

        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据配置加载类
     * @param basePath 路径
     * @param config 配置
     */
    public void createApplication(String basePath, AppConfigList.AppConfig config){
        String folderName = basePath + GlobalSetting.JAR_FOLDER;
        ClassLoader loader = this.jarLoader.createClassLoader(ApplicationManager.class.getClassLoader(), folderName);

        try {
            Class<?> appClass = loader.loadClass(config.getFile());

            IApplication app = (IApplication)appClass.newInstance();

            app.init();

            this.apps.put(config.getName(), app);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载
     * @param name 类名
     */
    public void reloadApplication(String name){
        IApplication oldApp = this.apps.remove(name);

        if(oldApp == null){
            return;
        }

        oldApp.destroy();

        AppConfigList.AppConfig config = this.configManager.getConfig(name);
        if(config == null){
            return;
        }

        createApplication(getBasePath(), config);
    }

    public static ApplicationManager getInstance(){
        if(instance == null){
            instance = new ApplicationManager();
        }
        return instance;
    }

    /**
     * 获取类
     * @param name 类名
     * @return 缓存中的类
     */
    public IApplication getApplication(String name){
        if(this.apps.containsKey(name)){
            return this.apps.get(name);
        }
        return null;
    }

    public String getBasePath(){
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
    }
}
