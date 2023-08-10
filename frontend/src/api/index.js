import axios from "axios";

const defaultFailure = (message, code, url) => {
    console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`)
}
const defaultError = (err) => {
    console.error("发生了一些错误，请联系管理员")
}

const internalPost = (url, data, header, success, failure, error) => {
    axios.post(url, data, {headers: header})
        .then(({data}) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        })
        .catch(err => error(err))
}


const internalGet = (url, header, success, failure, error) => {
    axios.get(url, {headers: header})
        .then(({data}) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        })
        .catch(err => error(err))
}

export const login = (username, password, remember, success, failure, error) => {
    internalPost('/api/auth/login', {
        username,
        password
    }, {
        'Content-Type': 'application/x-www-form-urlencoded'
    }, success, failure, error)
}
