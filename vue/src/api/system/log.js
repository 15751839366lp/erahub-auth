import service from "../../utils/request";

export function findLogList(params) {
    return service.request({
        url: 'system/log/findLogList',
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