#!/bin/bash

usage="使用方式：sh xxx.sh [option]\noption:\n\tgit-clone: 从git仓库克隆代码\n\tgit-pull: 从git仓库拉取代码\n\tgit-checkout: 切换分支，可带分支名称\n\tgit-branch: 获取分支信息\n\tdeploy: 部署项目\n\tstartup: 启动项目\n\tshutdown: 停止项目\n"

options=("git-clone"  "git-pull"  "git-checkout"  "git-branch"  "deploy"  "startup"  "shutdown")

# 项目参数
id="{{id}}"
name="{{name}}"
gitUrl="{{gitUrl}}"
branch="{{branch}}"
projectSourcePath="{{projectSourcePath}}"
projectDeployPath="{{projectDeployPath}}"
packageName="{{packageName}}"
deploySubModule="{{deploySubModule}}"

projectSourceDir=${projectSourcePath}${name}
projectDeployDir=${projectDeployPath}${name}
deployPackageName=${id}-${packageName}
deployPackage=${projectDeployPath}${deployPackageName}

## 定义基本方法
# 返回对象在数组中的下标，没有匹配值返回255。indexOf "test" "${arrs[*]}"
indexOf() {
  local text=$1
  local arr=$2
  local index=0

  for var in ${arr[*]}; do
    if [[ "$text" = "$var" ]]; then
      return $index
    fi

    let index++
  done
  return 255
}
checkSourcePathExists() {
  if [[ ! -d "${projectSourcePath}" ]]; then
    echo -e "\n目录不存在: ${projectSourcePath}"
    exit 1
  fi
}

##
option=$1
indexOf $option "${options[*]}"
if [[ $? = 255 ]];then
  echo -e $usage
  exit 1
fi

if [[ "$option" = "git-clone" ]]; then
  echo -e "克隆项目 ${name} ...\n"

  checkSourcePathExists

  cd ${projectSourcePath}
  git clone $gitUrl

elif [[ "$option" = "git-pull" ]]; then
  echo -e "拉取最新代码 ${name} ...\n"

  checkSourcePathExists

  cd ${projectSourceDir}
  git pull

elif [[ "$option" = "git-checkout" ]]; then
  echo -e "切换分支 ${name} ...\n"

  checkSourcePathExists

  cd ${projectSourceDir}
  if [[ ! -n "$branch" ]]; then
    git checkout master
  else
    git checkout $branch
  fi

elif [[ "$option" = "git-branch" ]]; then
  echo -e "获取branch信息 ${name} ...\n"

  checkSourcePathExists

  cd ${projectSourceDir}
  git branch

elif [[ "$option" = "deploy" ]]; then
  echo -e "部署项目 ${name} ...\n"

  checkSourcePathExists

  cd ${projectSourceDir}
  echo -e "使用mvn打包 ${name} ...\n"
  mvn clean install -U -Dmaven.test.skip=true

  echo -e "拷贝到目标目录 ${name} ...\n"
  if [[ ! -n "$deploySubModule" ]]; then
    cp target/${packageName} ${deployPackage}
  else
    cp ${deploySubModule}/target/${packageName}  ${deployPackage}
  fi

elif [[ "$option" = "startup" ]]; then
  echo -e "启动项目 ${name} ...\n"

  pid=$2
  if [[ -n "$pid" ]]; then
    echo -e "存在pid:$pid ,杀掉进程 ...\n"
    kill -s 9 $pid
  fi

  cd $projectDeployDir
  nohup java -jar $deployPackageName > nohup.out

elif [[ "$option" = "shutdown" ]]; then
  echo -e "停止项目 ${name} ...\n"

  pid=$2
  if [[ -n "$pid" ]]; then
    echo -e "存在pid:$pid ,杀掉进程 ...\n"
    kill -s 9 $pid
  fi
fi

echo -e "\n搞定\n"
