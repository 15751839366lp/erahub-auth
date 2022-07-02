package com.erahub.system.service;


import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Department;
import com.erahub.common.vo.system.DeanVO;
import com.erahub.common.vo.system.DepartmentVO;
import com.erahub.common.vo.common.PageVO;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/15 14:12
 * @Version 1.0
 **/
public interface DepartmentService {
    /**
     * 部门列表
     * @param pageNum
     * @param pageSize
     * @param departmentVO
     * @return
     */
    PageVO<DepartmentVO> findDepartmentList(Integer pageNum, Integer pageSize, DepartmentVO departmentVO);

    /**
     * 添加院部门
     * @param departmentVO
     */
    void add(DepartmentVO departmentVO) throws SystemException;

    /**
     * 编辑院部门
     * @param id
     * @return
     */
    DepartmentVO edit(Long id) throws SystemException;

    /**
     * 更新院部门
     * @param id
     * @param departmentVO
     */
    void update(Long id, DepartmentVO departmentVO) throws SystemException;

    /**
     * 删除院部门
     * @param id
     */
    void delete(Long id) throws SystemException;

    /**
     * 所有部门
     * @return
     */
    List<DepartmentVO> findAllVO();


    /**
     * 全部部门
     * @return
     */
    List<Department> findAll();

}
