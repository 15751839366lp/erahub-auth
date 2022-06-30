import service from "../../utils/request";

export function findRoleMenu(url) {
    return service.request({
        url: url,
        method: "get"
    });
}

export function findRoleList(params) {
    return service.request({
        url: 'system/role/findRoleList',
        method: 'get',
        params: params
    });
}

export function add(data) {
    return service.request({
        url: 'system/role/add',
        method: 'post',
        data
    });
}

export function editRole(url) {
    return service.request({
        url: url,
        method: 'get',
    });
}

export function update(url,data) {
    return service.request({
        url: url,
        method: 'put',
        data
    });
}

export function deleteRole(url) {
    return service.request({
        url: url,
        method: 'delete'
    });
}

export function updateStatus(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function authority(url,data) {
    return service.request({
        url: url,
        method: 'post',
        data
    });
}

export function excel() {
    return service.request({
        url: "system/role/excel",
        method: "post",
        responseType: "blob"
    });
}
