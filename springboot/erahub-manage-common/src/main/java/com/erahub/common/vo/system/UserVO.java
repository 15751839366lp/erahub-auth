package com.erahub.common.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2020/3/7 19:16
 * @Version 1.0
 **/
@Data
public class UserVO{

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String realname;

    private String email;

    private String phoneNumber;

    private Boolean status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull(message = "性别不能为空")
    private Integer sex;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    private Date birth;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String departmentName;

    @NotNull(message = "部门id不能为空")
    private Long departmentId;

}
