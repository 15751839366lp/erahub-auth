package com.erahub.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListMapUtils {
    public static <T> void copyList(Object source, List<T> target, Class<T> targetClassObj) {
        if ((!Objects.isNull(source)) && (!Objects.isNull(target))) {
            List list = (List) source;
            list.forEach(item -> {
                try {
                    T data = targetClassObj.newInstance();
                    BeanUtils.copyProperties(item, data);
                    target.add(data);
                } catch (InstantiationException e) {

                } catch (IllegalAccessException e) {

                }
            });
        }
    }

    //将对象转为map
    public Map<String, Object> objectToMap(Object obj) {
        return JSON.parseObject(JSON.toJSONString(obj), Map.class);
    }
}
