package com.lk.kDeploy.service;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class ProjectServiceTest {

	@Test
	public void getGitUrlByConfig() {
		String configStr = "[core]"
				+ "	repositoryformatversion = 0"
				+ "	filemode = false"
				+ "	bare = false"
				+ "	logallrefupdates = true"
				+ "	symlinks = false"
				+ "	ignorecase = true"
				+ "[remote \"origin\"]"
				+ "	url = git@gitlab.com:kathous/datacenter.git"
				+ "	fetch = +refs/heads/*:refs/remotes/origin/*"
				+ "[branch \"master\"]"
				+ "	remote = origin"
				+ "	merge = refs/heads/master"
				+ "[branch \"1.1.0\"]"
				+ "	remote = origin"
				+ "	merge = refs/heads/1.1.0"
				+ "[branch \"1.2.0\"]"
				+ "	remote = origin"
				+ "	merge = refs/heads/1.2.0";
		
		Pattern pattern = Pattern.compile("url = (\\S+)");
		Matcher matcher = pattern.matcher(configStr);
		matcher.find();
		
		String gitUtl = matcher.group(1);
		System.out.println(gitUtl);
		assertEquals("git@gitlab.com:kathous/datacenter.git", gitUtl);
	}

}
