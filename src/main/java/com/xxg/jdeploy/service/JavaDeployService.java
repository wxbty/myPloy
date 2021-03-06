package com.xxg.jdeploy.service;

import com.xxg.jdeploy.domain.JavaDeployInfo;
import com.xxg.jdeploy.mapper.JavaDeployMapper;
import com.xxg.jdeploy.util.ShellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
public class JavaDeployService {

    @Autowired
    private JavaDeployMapper javaDeployMapper;

    @Value("${shell.javadeploy}")
    private String shellFileFolder;

    @Value("${javadeploy.basepath}")
    private String basePath;

    public List<JavaDeployInfo> getList() {
        return javaDeployMapper.getList();
    }

    public JavaDeployInfo getDetail(String uuid) {
        return javaDeployMapper.getDetail(uuid);
    }

    public void insert(JavaDeployInfo javaDeployInfo) {
        javaDeployMapper.insert(javaDeployInfo);
    }

    public String getStatus(String uuid) throws IOException {
        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        if (info != null) {
            String out = "";
            if (StringUtils.isEmpty(info.getRemote_ip()))
                out = ShellUtil.exec("sh " + shellFileFolder + "/isrunning.sh " + info.getUuid());
            else
                out = ShellUtil.exec("sh " + shellFileFolder + "/isrunning_remote.sh " + info.getUuid() + " " + info.getRemote_ip());
            return String.valueOf(StringUtils.hasText(out) && out.contains("java -jar"));
        } else {
            return "false";
        }
    }

    public String deploy(String uuid) throws IOException {
        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        if (info != null) {
            StringBuilder sb = new StringBuilder();

            if (StringUtils.isEmpty(info.getRemote_ip())) {
                deployLocal(info, sb, "/package.sh");
            } else {
                deployRemote(info, sb, "/package_remote.sh");
            }

            return sb.toString();
        } else {
            return uuid + "对应的项目不存在！";
        }
    }

    public String deployPlus(String uuid) throws IOException {
        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        if (info != null) {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isEmpty(info.getRemote_ip())) {
                deployLocal(info, sb, "/packageGit.sh");
            } else {
                deployRemote(info, sb, "/packageGit_remote.sh");
            }

            return sb.toString();
        } else {
            return uuid + "对应的项目不存在！";
        }
    }

    public String restart(String uuid) throws IOException {

        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        boolean local = StringUtils.isEmpty(info.getRemote_ip());
        if (info != null) {
            StringBuilder sb = new StringBuilder();
            // kill进程
            if (local)
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/kill.sh " + info.getUuid()));
            else
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/kill_remote.sh " + info.getRemote_ip() + " " + info.getUuid()));

            String module = "";
            if (StringUtils.hasText(info.getModule())) {
                module = info.getModule() + '/';
            }
            String finalName = getFinalName(info, module);
            if (finalName != null) {
                // 启动程序
                if (local) {
                    if (StringUtils.hasText(info.getModule())) {
                        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_module.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
                    } else {
                        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
                    }
                } else {
                    if (StringUtils.hasText(info.getModule())) {
                        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_module_remote.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module + " " + info.getRemote_ip()));
                    } else {
                        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_remote.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module + " " + info.getRemote_ip()));
                    }
                }
            } else {
                sb.append("找不到程序包，请重新部署");
            }

            return sb.toString();
        } else {
            return uuid + "对应的项目不存在！";
        }
    }


    public String stop(String uuid) throws IOException {
        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        if (info != null) {
            if (StringUtils.isEmpty(info.getRemote_ip()))
                return ShellUtil.exec("sh " + shellFileFolder + "/kill.sh " + info.getUuid());
            else
                return ShellUtil.exec("sh " + shellFileFolder + "/kill_remote.sh " + info.getRemote_ip() + " " + info.getUuid());
        } else {
            return uuid + "对应的项目不存在！";
        }
    }


    public String showLog(String uuid) throws IOException {
        JavaDeployInfo info = javaDeployMapper.getDetail(uuid);
        if (info != null) {
            if (StringUtils.isEmpty(info.getRemote_ip()))
                return "sh " + shellFileFolder + "/showlog.sh " + info.getUuid() + " " + basePath;
            else
                return "sh " + shellFileFolder + "/showlog_remote.sh " + info.getUuid() + " " + basePath + " " + info.getRemote_ip();
        } else {
            return "echo \"对应的项目不存在！\"";
        }
    }


    private void deployRemote(JavaDeployInfo info, StringBuilder sb, String s) throws IOException {
        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/kill_remote.sh " + info.getRemote_ip() + " " + info.getUuid()));
        // 打包
        String[] cmdArray = {"sh", shellFileFolder + s, info.getUuid(), info.getUrl(), basePath, String.valueOf(info.getType()), info.getProfile(), info.getBranch(), info.getRemote_ip()};
        sb.append(ShellUtil.exec(cmdArray));
        String module = "";
        if (StringUtils.hasText(info.getModule())) {
            module = info.getModule() + '/';
        }
        String finalName = getFinalName(info, module);
        if (finalName != null) {
            // 启动程序
            if (StringUtils.hasText(info.getModule())) {
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_module_remote.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
            } else {
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_remote.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
            }
        } else {
            sb.append("打包失败");
        }
    }



    private void deployLocal(JavaDeployInfo info, StringBuilder sb, String s) throws IOException {
        sb.append(ShellUtil.exec("sh " + shellFileFolder + "/kill.sh " + info.getUuid()));
        // 打包
        String[] cmdArray = {"sh", shellFileFolder + s, info.getUuid(), info.getUrl(), basePath, String.valueOf(info.getType()), info.getProfile(), info.getBranch()};
        sb.append(ShellUtil.exec(cmdArray));
        String module = "";
        if (StringUtils.hasText(info.getModule())) {
            module = info.getModule() + '/';
        }
        String finalName = getFinalName(info, module);
        if (finalName != null) {

            // 启动程序
            if (StringUtils.hasText(info.getModule())) {
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start_module.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
            } else {
                sb.append(ShellUtil.exec("sh " + shellFileFolder + "/start.sh " + info.getUuid() + " " + finalName + " " + basePath + " " + module));
            }
        } else {
            sb.append("打包失败");
        }
    }




    private String getFinalName(JavaDeployInfo info, String module) throws IOException {
        String path = StringUtils.isEmpty(module) ? basePath + "/" + info.getUuid() + "/target" : basePath + "/" + info.getUuid() + "/" + module + "/target";
        String fileName = "";
        if (StringUtils.isEmpty(info.getRemote_ip()))
            fileName = ShellUtil.exec("sh " + shellFileFolder + "/showjar.sh " + path);
        else
            fileName = ShellUtil.exec("sh " + shellFileFolder + "/showjar_remote.sh " + path + " " + info.getRemote_ip());

        return fileName;
    }

}
