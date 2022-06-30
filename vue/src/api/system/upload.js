import service from "../../utils/request";

export function deleteImage(url) {
    return service.request({
        url: url,
        method: "delete"
    });
}

export function findImageList(params) {
    return service.request({
        url: "system/upload/findImageList",
        method: "get",
        params: params
    });
}
