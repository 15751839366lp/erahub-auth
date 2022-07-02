package com.erahub.common.model.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_user")
public class User {
    @ColumnWidth(20)
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "编号",index = 1)
    private Long id;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户名",index = 2)
    private String username;

    @ColumnWidth(40)
    @ExcelProperty(value = "真实姓名",index = 3)
    private String realname;

    @ColumnWidth(40)
    @ExcelProperty(value = "邮箱",index = 4)
    private String email;

    @ColumnWidth(40)
    @ExcelProperty(value = "电话号码",index = 5)
    private String phoneNumber;

    private Integer status;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "创建时间",index = 6)
    private Date createTime;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "修改时间",index = 7)
    private Date modifiedTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "性别",index = 8)
    private Integer sex;

    @ColumnWidth(40)
    @ExcelProperty(value = "密码盐值",index = 9)
    private String salt;

    @ColumnWidth(20)
    @ExcelProperty(value = "用户类型",index = 10)
    private Integer type;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户密码",index = 11)
    private String password;

    @ColumnWidth(40)
    @DateTimeFormat("yyyy/MM/dd")
    @ExcelProperty(value = "出生日期",index = 12)
    private Date birth;

    private Long departmentId;

    @ColumnWidth(60)
    @ExcelProperty(value = "头像url",index = 13)
    private String avatar;

}
