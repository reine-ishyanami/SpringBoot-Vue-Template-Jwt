const authItemName = "access_token"

export const storeAccessToken = (token, remember, expire) => {
    const authObj = {token: token, expire: expire}
    const str = JSON.stringify(authObj)
    if (remember) localStorage.setItem(authItemName, str)
    else sessionStorage.setItem(authItemName, str)
}

export const takeAccessToken = () => {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if (!str) return null
    const authObj = JSON.parse(str)
    if (authObj.expire <= new Date()) {
        deleteAccessToken()
        return false
    }
    return authObj.token
}


const deleteAccessToken = () => {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}