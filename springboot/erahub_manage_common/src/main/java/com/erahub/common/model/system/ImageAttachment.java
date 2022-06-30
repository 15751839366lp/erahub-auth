package com.erahub.common.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_image")
public class ImageAttachment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String path;

    private Long size;

    private String mediaType;

    private String suffix;

    private Integer height;

    private Integer width;

    private Date createTime;

}
