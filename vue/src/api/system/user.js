import service from "../../utils/request";

export function login(data) {
    return service.request({
        url: '/system/user/login',
        method: 'post',
        data
    });
}

export function info() {
    return service.request({
        url: '/system/user/info',
        method: 'get'
    });
}

export function findMenu() {
    return service.request({
        url: '/system/user/findMenu',
        method: 'get'
    });
}

export function getRoles(url) {
    return service.request({
        url: url,
        method: 'get'
    });
}

export function assignRoles(url,data) {
    return service.request({
        url: url,
        method: 'post',
        data
    });
}

export function findUserList(params) {
    return service.request({
        url: 'system/user/findUserList',
        method: 'get',
        params: params
    });
}

export function deleteUser(url) {
    return service.request({
        url: url,
        method: 'delete'
    });
}

export function add(data) {
    return service.request({
        url: 'system/user/add',
        method: 'post',
        data: data
    });
}

export function changeUserPassword(data) {
    return service.request({
        url: 'system/user/changeUserPassword',
        method: 'post',
        data: data
    });
}

export function update(url,data) {
    return service.request({
        url: url,
        method: 'put',
        data
    });
}

export function editUser(url) {
    return service.request({
        url: url,
        method: 'get',
    });
}

export function updateStatus(url) {
    return service.request({
        url: url,
        method: 'put',
    });
}

export function findAll() {
    return service.request({
        url: "system/department/findAll",
        method: 'get',
    });
}

export function excel() {
    return service.request({
        url: "system/user/excel",
        method: "post",
        responseType: "blob"
    });
}
