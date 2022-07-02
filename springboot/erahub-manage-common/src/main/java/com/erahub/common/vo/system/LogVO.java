package com.erahub.common.vo.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by lipeng on 2019/11/15 17:29
 */
@Data
public class LogVO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private Long time;

    private String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String location;

    private String operation;

    private String method;

    private String params;
}
