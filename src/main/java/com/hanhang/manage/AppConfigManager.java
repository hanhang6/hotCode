package com.hanhang.manage;

import com.hanhang.config.AppConfigList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanhang
 */
public class AppConfigManager {
    private final List<AppConfigList.AppConfig> configs;

    public AppConfigManager(){
        configs = new ArrayList<>();
    }

    /**
     * 加载配置
     * @param path 路径
     */
    public void loadAllApplicationConfigs(URI path){

        File file = new File(path);
        XStream xstream = getXmlDefine();
        try {
            AppConfigList configList = (AppConfigList)xstream.fromXML(new FileInputStream(file));

            if(configList.getConfigs() != null){
                this.configs.addAll(new ArrayList<>(configList.getConfigs()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取xml配置定义
     * @return XStream
     */
    private XStream getXmlDefine(){
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("apps", AppConfigList.class);
        xstream.alias("app", AppConfigList.AppConfig.class);
        xstream.aliasField("name", AppConfigList.AppConfig.class, "name");
        xstream.aliasField("file", AppConfigList.AppConfig.class, "file");
        xstream.addImplicitCollection(AppConfigList.class, "configs");
        Class<?>[] classes = new Class[] {AppConfigList.class,AppConfigList.AppConfig.class};
        xstream.allowTypes(classes);
        return xstream;
    }

    public final List<AppConfigList.AppConfig> getConfigs() {
        return configs;
    }

    public AppConfigList.AppConfig getConfig(String name){
        for(AppConfigList.AppConfig config : this.configs){
            if(config.getName().equalsIgnoreCase(name)){
                return config;
            }
        }
        return null;
    }
}
