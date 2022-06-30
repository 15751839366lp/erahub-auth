package com.erahub.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.common.enums.system.UserStatusEnum;
import com.erahub.common.enums.system.UserTypeEnum;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Department;
import com.erahub.common.model.system.Role;
import com.erahub.common.model.system.User;
import com.erahub.common.model.system.UserRole;
import com.erahub.common.vo.system.DeanVO;
import com.erahub.common.vo.system.DepartmentVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.system.converter.DepartmentConverter;
import com.erahub.system.mapper.DepartmentMapper;
import com.erahub.system.mapper.RoleMapper;
import com.erahub.system.mapper.UserMapper;
import com.erahub.system.mapper.UserRoleMapper;
import com.erahub.system.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/15 14:15
 * @Version 1.0
 **/
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 系别列表
     *
     * @param pageNum
     * @param pageSize
     * @param departmentVO
     * @return
     */
    @Override
    public PageVO<DepartmentVO> findDepartmentList(Integer pageNum, Integer pageSize, DepartmentVO departmentVO) {
        IPage<Department> departmentIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        if (departmentVO.getName() != null && !"".equals(departmentVO.getName())) {
            wrapper.like("name", departmentVO.getName());
        }
        departmentIPage = departmentMapper.selectPage(departmentIPage, wrapper);
        List<Department> departments = departmentIPage.getRecords();
        //转vo
        List<DepartmentVO> departmentVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departments)) {
            for (Department department : departments) {
                DepartmentVO d = new DepartmentVO();
                BeanUtils.copyProperties(department, d);
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("department_id", department.getId());
                userQueryWrapper.ne("type", UserTypeEnum.SYSTEM_ADMIN.getTypeCode());

                d.setTotal(userMapper.selectCount(userQueryWrapper));
                departmentVOS.add(d);
            }
        }

        return new PageVO<>(departmentIPage.getTotal(), departmentVOS);
    }

    /**
     * 添加院系
     * @param departmentVO
     */
    @Override
    public void add(DepartmentVO departmentVO) throws SystemException {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("name", departmentVO.getName());
        int i = departmentMapper.selectCount(wrapper);
        if (i != 0) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该部门名已被占用");
        }

        Department department = new Department();
        BeanUtils.copyProperties(departmentVO,department);
        department.setCreateTime(new Date());
        department.setModifiedTime(new Date());
        departmentMapper.insert(department);
    }

    /**
     * 编辑院系
     * @param id
     * @return
     */
    @Override
    public DepartmentVO edit(Long id) throws SystemException {
        Department department = departmentMapper.selectById(id);
        if(department==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"编辑的部门不存在");
        }
        return DepartmentConverter.converterToDepartmentVO(department);
    }

    /**
     * 更新部门
     * @param id
     * @param departmentVO
     */
    @Override
    public void update(Long id, DepartmentVO departmentVO) throws SystemException {
        Department dbDepartment = departmentMapper.selectById(id);
        if(dbDepartment==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要更新的部门不存在");
        }

        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("name", departmentVO.getName());
        int i = departmentMapper.selectCount(wrapper);
        if (i != 0) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该部门名已被占用");
        }

        Department department = new Department();
        BeanUtils.copyProperties(departmentVO,department);
        department.setId(id);
        department.setModifiedTime(new Date());
        departmentMapper.updateById(department);
    }

    /**
     * 删除部门信息
     * @param id
     */
    @Override
    public void delete(Long id) throws SystemException {
        Department department = departmentMapper.selectById(id);
        if(department==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要删除的部门不存在");
        }
        departmentMapper.deleteById(id);
    }

    @Override
    public List<DepartmentVO> findAllVO() {
        List<Department> departments = departmentMapper.selectList(null);
        //转vo
        List<DepartmentVO> departmentVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departments)) {
            for (Department department : departments) {
                DepartmentVO d = new DepartmentVO();
                BeanUtils.copyProperties(department, d);
                QueryWrapper<User> userQueryWrapper= new QueryWrapper<>();
                userQueryWrapper.eq("department_id",department.getId());
                userQueryWrapper.ne("type",0);

                d.setTotal(userMapper.selectCount(userQueryWrapper));
                departmentVOS.add(d);
            }
        }
        return departmentVOS;
    }

    @Override
    public List<Department> findAll() {
        return departmentMapper.selectList(null);
    }
}
