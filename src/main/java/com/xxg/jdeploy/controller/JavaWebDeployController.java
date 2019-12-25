package com.xxg.jdeploy.controller;

import com.xxg.jdeploy.service.JavaWebDeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * 线上环境的后台服务自动化部署
 * @author wxb
 */
@Controller
public class JavaWebDeployController {
	
	@Autowired
	private JavaWebDeployService javaWebDeployService;

	/**
	 * 添加项目页面
	 */
	@ResponseBody
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
	public String newService() {
		return "index";
	}

	/**
	 * ajax部署
	 */
	@ResponseBody
	@RequestMapping(value = "/deploy", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
	public String ajaxDeploy(String name, String giturl,String branch,int port) throws IOException {
		return HtmlUtils.htmlEscape(javaWebDeployService.deploy(name,giturl,branch,port));
	}


}
