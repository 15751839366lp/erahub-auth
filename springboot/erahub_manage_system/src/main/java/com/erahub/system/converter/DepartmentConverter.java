package com.erahub.system.converter;

import com.erahub.common.model.system.Department;
import com.erahub.common.vo.system.DepartmentVO;
import org.springframework.beans.BeanUtils;

/**
 * @Author lipeng
 * @Date 2020/3/7 19:56
 * @Version 1.0
 **/
public class DepartmentConverter {


    /**
     * 转vo
     * @return
     */
    public static DepartmentVO converterToDepartmentVO(Department department){
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department,departmentVO);
        return departmentVO;
    }
}
