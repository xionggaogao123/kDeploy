window.$api = (url, method, param, fn) => {
  axios({
    method: method,
    url: url,
    data: param
  }).then((response) => {
    console.log(response)
    if (typeof fn === "function") {
      fn(response)
    }

    if (data.returnCode != 0) {
      console.warn(data.returnMsg)
    }
  })
}
