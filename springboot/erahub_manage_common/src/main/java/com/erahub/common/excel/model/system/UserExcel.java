package com.erahub.common.excel.model.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

@Data
public class UserExcel {
    @ColumnWidth(20)
    @ExcelProperty(value = "编号")
    private Long id;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户名")
    private String username;

    @ColumnWidth(40)
    @ExcelProperty(value = "真实姓名")
    private String realname;

    @ColumnWidth(40)
    @ExcelProperty(value = "邮箱")
    private String email;

    @ColumnWidth(40)
    @ExcelProperty(value = "电话号码")
    private String phoneNumber;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "修改时间")
    private Date modifiedTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "性别")
    private Integer sex;

    @ColumnWidth(40)
    @ExcelProperty(value = "密码盐值")
    private String salt;

    @ColumnWidth(20)
    @ExcelProperty(value = "用户类型")
    private Integer type;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户密码")
    private String password;

    @ColumnWidth(40)
    @DateTimeFormat("yyyy/MM/dd")
    @ExcelProperty(value = "出生日期")
    private Date birth;

    @ColumnWidth(60)
    @ExcelProperty(value = "头像url")
    private String avatar;
}
