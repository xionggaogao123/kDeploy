package com.lk.kDeploy.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.base.vo.ProjectListVO;

/**
 * 项目命令业务层
 * linux版本
 *
 * @author: lk
 * @since: 2017年11月28日
 */
@Service
public class ProjectCommandService {

	@Autowired
	private CommandService commandService;
	
	public Integer getStatus(String id) {
		if (getStartedProjectIds().contains(id)) {
			return 1;
		}
		return 0;
	}

	public void setStatus(List<ProjectListVO> resList) {
		Set<String> startedSet = getStartedProjectIds();
		
		resList.forEach(project -> {
			if (startedSet.contains(project.getId())) {
				project.setStatus(1);
				return;
			}
			project.setStatus(0);
		});
	}

	/**
	 * 获取存在线程的项目id
	 * @return
	 */
	private Set<String> getStartedProjectIds() {
		String echo = commandService.execute("ps -ef | grep java");
		
		String[] split = echo.split("\n");
		Set<String> set = new HashSet<>();
		for (String processStr : split) {
			if (StringUtils.isBlank(processStr)) {
				continue;
			}
			
			String id = getProjectId(processStr);
			if (id.length() == 36) {
				set.add(id);
			}
		}
		return set;
	}
	private String getProjectId(String processStr) {
		Pattern pattern = Pattern.compile("\\s([a-z0-9_]+)-\\S+(\\.jar|\\.war)\\s");
		Matcher matcher = pattern.matcher(processStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
}
