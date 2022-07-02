package com.erahub.common.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import java.util.Date;

@Data
@Excel(value = "菜单表格")
@TableName("tb_menu")
public class Menu {

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "编号", width = 50)
    private Long id;

    @ExcelField(value = "父级id", width = 50)
    private Long parentId;

    @ExcelField(value = "菜单名称", width = 100)
    private String menuName;

    @ExcelField(value = "菜单url", width = 100)
    private String url;

    @ExcelField(value = "菜单图标", width = 80)
    private String icon;

    @ExcelField(value = "是否展开", width = 50)
    private Integer open;

    @ExcelField(value = "菜单类型", width = 80)
    private Integer type;

    @ExcelField(value = "排序", width = 90)
    private Long orderNum;

    @ExcelField(value = "创建时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date createTime;

    @ExcelField(value = "修改时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date modifiedTime;

    @ExcelField(value = "是否可用",width = 80)
    private Integer available;

    @ExcelField(value = "权限编码", width = 180)
    private String perms;
}
