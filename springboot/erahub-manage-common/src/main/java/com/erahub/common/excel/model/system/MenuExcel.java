package com.erahub.common.excel.model.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 18:12
 * @Version 1.0
 **/
@Data
public class MenuExcel {


    @ColumnWidth(10)
    @ExcelProperty(value = "ID")
    private Long id;

    @ColumnWidth(10)
    @ExcelProperty(value = "父节点")
    private Long parentId;

    @ColumnWidth(20)
    @ExcelProperty(value = "菜单名称")
    private String menuName;

    @ColumnWidth(20)
    @ExcelProperty(value = "菜单URL")
    private String url;

    @ColumnWidth(20)
    @ExcelProperty(value = "权限标识")
    private String perms;

    @ColumnWidth(20)
    @ExcelProperty(value = "图标")
    private String icon;

    @ColumnWidth(20)
    @ExcelProperty(value = "类型")
    private Integer type;

    @ColumnWidth(20)
    @ExcelProperty(value = "排序")
    private Long orderNum;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "修改时间")
    private Date modifiedTime;

    @ColumnWidth(10)
    @ExcelProperty(value = "可用")
    private Integer available;

    @ColumnWidth(20)
    @ExcelProperty(value = "展开")
    private Integer open;
}
