package com.lk.kDeploy.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lk.kDeploy.base.vo.ProjectListVO;

/**
 * 项目命令业务层
 * linux版本
 *
 * @author: lk
 * @since: 2017年11月28日
 */
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

	private Set<String> getStartedProjectIds() {
		String execute = commandService.execute("ps -ef | grep java");
		// TODO
		return new HashSet<>();
	}
}
