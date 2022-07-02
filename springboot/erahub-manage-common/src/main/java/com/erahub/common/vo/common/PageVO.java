package com.erahub.common.vo.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2020/3/7 19:41
 * @Version 1.0
 **/
@Data
public class PageVO<T> {
    private long total;

    private List<T> rows = new ArrayList<>();

    private List<String> data = new ArrayList<>();

    private Map<String,String> map = new HashMap<>();

    public PageVO(long total, List<T> data) {
        this.total = total;
        this.rows = data;
    }

    public PageVO(long total, List<T> rows,List<String> data) {
        this.total = total;
        this.rows = rows;
        this.data = data;
    }
    public PageVO(long total, List<T> rows,List<String> data,Map<String,String> map) {
        this.total = total;
        this.rows = rows;
        this.data = data;
    }

}
