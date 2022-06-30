import service from "../../utils/request";

export function loginReport(data) {
    return service.request({
        url: '/system/loginLog/loginReport',
        method: 'post',
        data
    });
}

export function findLoginLogList(params) {
    return service.request({
        url: 'system/loginLog/findLoginLogList',
        method: 'get',
        params: params
    });
}

export function deleteLog(url) {
    return service.request({
        url: url,
        method: 'delete'
    });
}

export function batchDelete(url) {
    return service.request({
        url: url,
        method: 'delete'
    });
}