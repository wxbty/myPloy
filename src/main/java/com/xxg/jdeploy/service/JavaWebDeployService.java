package com.xxg.jdeploy.service;

import com.xxg.jdeploy.util.ShellUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JavaWebDeployService {


    @Value("${shell.javawebdeploy}")
    private String shellFileFolder;

    @Value("${javawebdeploy.basepath}")
    private String basePath;

    @Value("${javawebdeploy.jettypath}")
    private String jettyPath;


    public String deploy(String name, String giturl, String branch, int port) throws IOException {
        StringBuilder sb = new StringBuilder();

        // kill进程
        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/kill.sh " + name));
        // 打包
        String contextPath = "/";
        contextPath = contextPath.replace("/", "");
        if (contextPath.length() == 0) {
            contextPath = "root";
        }

        String[] cmdArray = {"sh", shellFileFolder + "/package.sh", name, giturl, jettyPath, basePath, "git", "null", branch};
        sb.append(ShellUtil.exec(cmdArray));

        String module = "";


        String finalName = getFinalName(module, name);
        if (finalName != null) {
            FileUtils.copyFile(new File(basePath + "/" + name + module + "/target/" + finalName), new File(basePath + "/" + name + "/webapps/" + contextPath + ".war"));
            // 启动程序
            sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start.sh " + name + " " + port + " " + jettyPath + " " + basePath));
        } else {
            sb.append("打包失败");
        }
        return sb.toString();

    }

    private String getFinalName(String module, String uuid) {
        File dir = new File(basePath + "/" + uuid + module + "/target");
        File[] files = dir.listFiles();

        String fileName = null;
        for (File file : files) {
            String name = file.getName();
            if (file.isFile() && name.endsWith(".war")) {
                fileName = name;
            }
        }
        return fileName;
    }
}
