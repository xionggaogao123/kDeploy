window.$api = (url, method, param, fn) => {
  axios({
    method: method,
    url: url,
    data: param
  }).then((resp) => {
    console.log(resp)
    let data = resp.data

    if (typeof fn === "function") {
      fn(data)
    }

    if (data.returnCode != 0) {
      console.warn(data.returnMsg)
    }
  })
}
