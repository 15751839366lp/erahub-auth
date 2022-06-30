package com.erahub.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.ImageAttachment;
import com.erahub.common.vo.system.ImageAttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author lipeng
 * @Date 2020/8/19 14:57
 * @Version 1.0
 **/
public interface UploadService {

    String uploadImage(MultipartFile file) throws IOException, SystemException; //图片上传

    IPage<ImageAttachment> findImageList(Integer pageNum, Integer pageSize,ImageAttachmentVO imageAttachmentVO); //图片列表

    void delete(Long id) throws SystemException; //删除图片

}
