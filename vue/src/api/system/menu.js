import service from "../../utils/request";

export function update(url,data) {
    return service.request({
        url: url,
        method: "put",
        data
    });
}

export function editMenu(url) {
    return service.request({
        url: url,
        method: "get"
    });
}

export function tree() {
    return service.request({
        url: "system/menu/tree",
        method: "get"
    });
}

export function deleteMenu(url) {
    return service.request({
        url: url,
        method: "delete"
    });
}

export function add(data) {
    return service.request({
        url:"system/menu/add",
        method: "post",
        data
    });
}

export function excel() {
    return service.request({
        url: "system/menu/excel",
        method: "post",
        responseType: "blob"
    });
}
