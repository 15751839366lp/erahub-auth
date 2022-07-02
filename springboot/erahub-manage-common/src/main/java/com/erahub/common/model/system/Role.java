package com.erahub.common.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import java.util.Date;

@Excel(value = "角色表格")
@Data
@TableName("tb_role")
public class Role {

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "编号", width = 50)
    private Long id;

    @ExcelField(value = "角色名称", width = 100)
    private String roleName;

    @ExcelField(value = "备注信息", width = 180)
    private String remark;

    @ExcelField(value = "创建时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date createTime;

    @ExcelField(value = "修改时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date modifiedTime;

    @ExcelField(value = "禁用状态", width = 50)
    private Integer status;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
