# kDeploy接口文档

## 基础API

* kDeploy系统的接口调用地址为："`域名`:`端口号`/kDeploy/api/`uri`"
* API一律使用`HTTP POST`发出请求。各个请求的参数格式在本文档中有定义。
* JSON中的时间，一律是 `yyyy-MM-dd HH:mm:ss` 格式，东八区时间。
* JSON中的密码，一律是 `SHA512 HEX`字符串格式。

### 请求数据格式
~~~js
{
	page: 1, // 第几页，从1开始计数。分页的接口必填
	pageSize: 20 // 本次分页大小。分页的接口必填
	params: {} // 请求参数
}
~~~

### 返回数据格式
前后台交互有三种返回数据格式：

1. 操作结果返回格式
~~~js
${操作结果}
{
	code: 0, // 如果请求成功，code=0；否则表示请求失败
	message: '' // 如果code!=0，给出失败原因
}
~~~

2. 查询单条数据返回格式
~~~js
${查询单条数据}
{
	code: 0, // 如果请求成功，code=0；否则表示请求失败
	message: '', // 如果code!=0，给出失败原因

	data: ${SOME DATA}
}
~~~

3. 查询多条数据返回格式
~~~js
${查询多条数据}
{
	code: 0, // 如果请求成功，code=0；否则表示请求失败
	message: '', // 如果code!=0，给出失败原因

	totalPage: 100, // 总页数
	page: 1, // 第几页，从1开始计数
	pageSize: 20, // 本次分页大小

	dataList: [${SOME DATA}, ${SOME DATA}, ...]
}
~~~

## 用户模块

### 登录 `/user/login`
~~~js
Request:
{
	username: '', // 账号
	password: '' // 密码。SHA512加密
}

Response:
${操作结果}
~~~

### 登录 `/user/logout`
~~~js
Response:
${操作结果}
~~~

## 项目模块

### 分页查询项目列表 `/project/pageList`
~~~js
Request:
{
	name: '', // 选填。项目名模糊匹配
}

Response:
${查询多条数据} -> {
  id: "",
  name: "", // 项目名
  description: "", // 描述
  status: 0, // 状态。0 未部署, 1 启动中, 2 已停止
}
~~~

### 查询项目详情 `/project/{id}/get`
{id}为项目id
~~~js
Response:
${查询单条数据} -> {
  id: "",
  name: "",
  description: "",
  gitUrl: "",
  branch: "",
  projectSourcePath: "",
  projectDeployPath: "",
  packageName: "",
  deploySubModule: "",
  deployWay: "",
  customStart: "",
  webContainerId: "",
  createAt: "",
  status: 0, // 状态。0 未部署, 1 启动中, 2 已停止
}
~~~

### 新增项目 `/project/add`
~~~js
Response:
{
  name: "",
  description: "",
  gitUrl: "",
  branch: "",
  projectSourcePath: "",
  projectDeployPath: "",
  packageName: "",
  deploySubModule: "",
  deployWay: "",
  customStart: "",
  webContainerId: "",
}

Response:
${操作结果}
~~~

### 更新项目 `/project/{id}/update`
{id}为项目id
~~~js
Response:
{
  name: "", // 全部更新参数选填
  description: "",
  gitUrl: "",
  branch: "",
  projectSourcePath: "",
  projectDeployPath: "",
  packageName: "",
  deploySubModule: "",
  deployWay: "",
  customStart: "",
  webContainerId: "",
}

Response:
${操作结果}
~~~

### 删除项目 `/project/{id}/delete`
{id}为项目id
~~~js
Response:
${操作结果}
~~~
