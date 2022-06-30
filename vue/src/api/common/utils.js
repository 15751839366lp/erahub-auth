function isEmpty(obj) {
    if (obj == null || obj == undefined || obj === '') {
        return true
    }
    return false
}

function isStringIneger(number) {
    if (isEmpty(number)) {
        return false
    }
    const reg = /^[+]?(0|([0-9]\d*))?$/;
    return reg.test(number)
}

function isIneger(number) {
    if (isEmpty(number)) {
        return false
    }
    const reg = /^[+]?(0|([1-9]\d*))?$/;
    return reg.test(number)
}

function isNumberTwoScale(number) {
    if (isEmpty(number)) {
        return false
    }
    const reg = /^[+-]?(0|([1-9]\d*))(\.\d{1,2})?$/;
    return reg.test(number)
}

function isPercentageTwoScale(number) {
    if (isEmpty(number)) {
        return false
    }
    const reg = /^(\d|[1-9]\d|100)(\.\d{1,2})?$/;
    return reg.test(number)
}

function isNumberBetweenZeroToOne(number) {
    if (isEmpty(number)) {
        return false
    }
    const reg = /^(0(\.\d{1,2})?|1(\.0{1,2})?)$/;
    return reg.test(number)
}

function camelToSnakeCase(str) {
    return str.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
}

function cloneObj(obj,newObj) {
    if (obj instanceof Array) {
        newObj = [];
    }
    for (const key in obj) {
        const val = obj[key];
        if(val == null || val == undefined){
            newObj[key] = val;
        }else{
            //newObj[key] = typeof val === 'object' ? arguments.callee(val) : val; //arguments.callee 在哪⼀个函数中运⾏，它就代表哪个函数, ⼀般⽤在匿名函数中。
            let objChild = {};
            newObj[key] = typeof val === 'object' ? cloneObj(val,objChild): val;
        }
    }
    return newObj;
}


export default {
    isEmpty,

    isStringIneger,
    isIneger,
    isNumberTwoScale,
    isPercentageTwoScale,
    isNumberBetweenZeroToOne,

    camelToSnakeCase,

    cloneObj,
}