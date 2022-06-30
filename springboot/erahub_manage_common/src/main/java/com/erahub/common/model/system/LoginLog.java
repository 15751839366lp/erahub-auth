package com.erahub.common.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_login_log")
public class LoginLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private Date loginTime;

    private String location;

    private String ip;

    private String userSystem;

    private String userBrowser;

}
