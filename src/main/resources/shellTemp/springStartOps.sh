#!/bin/bash

usage="使用方式：sh xxx.sh [option]\noption:\n\tgit-clone: 从git仓库克隆代码\n\tgit-pull: 从git仓库拉取代码\n\tgit-checkout: 切换分支，可带分支名称"

options=("git-clone"  "git-pull"  "git-checkout")

# 项目参数
id="30e16761_df4c_437d_9b48_b663856da159"
name="springStart"
gitUrl="git@120.77.209.205:repositories/springStart.git"
branch=
projectSourcePath="/home/dev/kdeployDir/"
projectDeployPath="/home/dev/kdeployApplDir/"
packageName="sprintStart-0.0.1-SNAPSHOT.jar"
deploySubModule=

projectSourceDir=${projectSourcePath}${name}
projectDeployDir=${projectDeployPath}${name}

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

##
option=$1
indexOf $option "${options[*]}"
if [[ $? = 255 ]];then
  echo -e $usage
  exit 1
fi

if [[ "$option" = "git-clone" ]]; then
  echo "克隆项目 ${name} ...\n"

  if [[ ! -d "${projectSourcePath}" ]]; then
    echo -e "\n目录不存在: ${projectSourcePath}"
    exit 1
  fi

  cd ${projectSourcePath}
  git clone $gitUrl

elif [[ "$option" = "git-pull" ]]; then
  echo -e "拉取最新代码 ${name} ...\n"
  #TODO

elif [[ "$option" = "git-checkout" ]]; then
  echo -e "切换分支 ${name} ...\n"
  #TODO

fi
