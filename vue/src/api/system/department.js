import service from "../../utils/request";

export function update(url,data) {
    return service.request({
        url: url,
        method: "put",
        data
    });
}

export function edit(url) {
    return service.request({
        url: url,
        method: "get"
    });
}

export function findDepartmentList(params) {
    return service.request({
        url: "system/department/findDepartmentList",
        method: "get",
        params: params
    });
}

export function deleteDepartment(url) {
    return service.request({
        url: url,
        method: "delete"
    });
}

export function add(data) {
    return service.request({
        url:"system/department/add",
        method: "post",
        data
    });
}

export function excel() {
    return service.request({
        url: "system/department/excel",
        method: "post",
        responseType: "blob"
    });
}
