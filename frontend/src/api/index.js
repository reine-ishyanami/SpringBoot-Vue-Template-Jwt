import axios from "axios";
import {takeAccessToken, deleteAccessToken, storeAccessToken} from "@/api/token";

const defaultFailure = (message, code, url) => console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`)
const defaultError = () => console.error("发生了一些错误，请联系管理员")


const accessHeader = () => {
    const token = takeAccessToken()
    return token ? {'Authorization': `Bearer ${token}`} : {}
}

const internalPost = (url, data, header, success, failure = defaultFailure, error = defaultError) => {
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

const get = (url, success, failure = defaultFailure) => internalGet(url, accessHeader(), success, failure)
const post = (url, data, success, failure = defaultFailure) => internalPost(url, data, accessHeader(), success, failure)

export const unauthorized = () => !takeAccessToken()

export const login = ({username, password, remember}, success, failure, error) => {
    internalPost('/api/auth/login', {
        username,
        password
    }, {
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data) => {
        storeAccessToken(data.token, remember, data.expire)
        success(data)
    }, failure, error)
}

export const logout = (success, failure) => {
    get('/api/auth/logout', () => {
        deleteAccessToken()
        success()
    }, failure)
}

export const askRegisterCode = (email, success, failure) => {
    internalGet(`/api/auth/ask-code?email=${email}&type=register`, {}, success, failure)
}

export const askResetCode = (email, success, failure) => {
    internalGet(`/api/auth/ask-code?email=${email}&type=reset`, {}, success, failure)
}

export const register = ({username, email, password, code}, success, failure) => {
    internalPost('/api/auth/register', {
        username, email, password, code
    }, {}, success, failure)
}

export const resetConfirm = ({email, code}, success, failure) => {
    internalPost('/api/auth/reset-confirm', {
        email, code
    }, {}, success, failure)
}

export const resetPassword = ({email, password, code}, success, failure) => {
    internalPost('/api/auth/reset-password', {
        email, password, code
    }, {}, success, failure)
}