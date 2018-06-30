package com.xxg.jdeploy.mapper;

import com.xxg.jdeploy.domain.JavaDeployInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JavaDeployMapper {

	@Select("select uuid,name,url,type,remote_ip from java_deploy")
	List<JavaDeployInfo> getList();

	@Select("select uuid,name,url,type,profile,module,branch,remote_ip from java_deploy where uuid=#{uuid}")
	JavaDeployInfo getDetail(String uuid);

	@Insert("insert into java_deploy (uuid,name,url,type,profile,module,branch,remote_ip) values (#{uuid},#{name},#{url},#{type},#{profile},#{module},#{branch},#{remote_ip})")
	void insert(JavaDeployInfo javaDeployInfo);

}