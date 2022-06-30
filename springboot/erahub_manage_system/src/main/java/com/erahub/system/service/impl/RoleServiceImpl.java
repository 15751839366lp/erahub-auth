package com.erahub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.common.enums.system.RoleStatusEnum;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Menu;
import com.erahub.common.model.system.Role;
import com.erahub.common.model.system.RoleMenu;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.system.RoleVO;
import com.erahub.system.converter.RoleConverter;
import com.erahub.system.mapper.MenuMapper;
import com.erahub.system.mapper.RoleMapper;
import com.erahub.system.mapper.RoleMenuMapper;
import com.erahub.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/9 16:23
 * @Version 1.0
 **/
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;


    @Autowired
    private MenuMapper menuMapper;

    /**
     * 角色列表
     * @param pageNum
     * @param pageSize
     * @param roleVO
     * @return
     */
    @Override
    public PageVO<RoleVO> findRoleList(Integer pageNum, Integer pageSize, RoleVO roleVO) {
        IPage<Role> roleIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

        String roleName = roleVO.getRoleName();
        if (roleName!=null&&!"".equals(roleName)){
            roleQueryWrapper.like("role_name",roleName);
        }
        roleIPage = roleMapper.selectPage(roleIPage, roleQueryWrapper);
        List<Role> roles = roleIPage.getRecords();
        List<RoleVO> roleVOS= RoleConverter.converterToRoleVOList(roles);

        return new PageVO<>(roleIPage.getTotal(),roleVOS);
    }

    /**
     * 添加角色
     * @param roleVO
     */
    @Override
    public void add(RoleVO roleVO) throws SystemException {
        @NotBlank(message = "角色名必填") String roleName = roleVO.getRoleName();
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_name",roleName);
        int i = roleMapper.selectCount(roleQueryWrapper);
        if(i!=0){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"该角色名已被占用");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleVO,role);
        role.setCreateTime(new Date());
        role.setModifiedTime(new Date());
        role.setStatus(RoleStatusEnum.AVAILABLE.getStatusCode());//默认启用添加的角色
        roleMapper.insert(role);
    }

    /**
     * 删除角色
     * @param id
     */
    @Transactional
    @Override
    public void deleteById(Long id) throws SystemException {
        Role role = roleMapper.selectById(id);
        if(role==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要删除的角色不存在");
        }
        roleMapper.deleteById(id);
        //删除对应的[角色-菜单]记录
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id",id);
        roleMenuMapper.delete(roleMenuQueryWrapper);
    }

    /**
     * 编辑角色信息
     * @param id
     * @return
     */
    @Override
    public RoleVO edit(Long id) throws SystemException {
        Role role = roleMapper.selectById(id);
        if(role==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"编辑的角色不存在");
        }

        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role,roleVO);
        return roleVO;
    }

    /**
     * 更新角色信息
     * @param id
     * @param roleVO
     */
    @Override
    public void update(Long id, RoleVO roleVO) throws SystemException {
        @NotBlank(message = "角色名必填") String roleName = roleVO.getRoleName();
        Role dbRole = roleMapper.selectById(id);
        if(dbRole==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要更新的角色不存在");
        }
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_name",roleName);

        List<Role> roles = roleMapper.selectList(roleQueryWrapper);
        if(!CollectionUtils.isEmpty(roles)){
            Role role = roles.get(0);
            if(!role.getId().equals(id)){
                throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"该角色名已被占用");
            }
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleVO,role);
        role.setModifiedTime(new Date());
        roleMapper.updateById(role);
    }

    /**
     * 角色状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) throws SystemException {
        Role role = roleMapper.selectById(id);
        if(role==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"角色不存在");
        }
        Role t = new Role();
        t.setId(id);
        t.setStatus(status?RoleStatusEnum.DISABLE.getStatusCode():
                RoleStatusEnum.AVAILABLE.getStatusCode());
        roleMapper.updateById(t);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null);
    }

    /**
     * 获取角色已有权限id
     * @param id
     * @return
     */
    @Override
    public List<Long> findMenuIdsByRoleId(Long id) throws SystemException {
        Role role = roleMapper.selectById(id);
        if(role==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"该角色已不存在");
        }
        List<Long> ids=new ArrayList<>();
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id",id);

        List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuQueryWrapper);
        if(!CollectionUtils.isEmpty(roleMenus)){
            for (RoleMenu roleMenu : roleMenus) {
                ids.add(roleMenu.getMenuId());
            }
        }
        return ids;
    }

    /**
     * 角色授权
     * @param id
     * @param mids
     */
    @Transactional
    @Override
    public void authority(Long id,Long[] mids) throws SystemException {
        Role role = roleMapper.selectById(id);
        if(role==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"该角色不存在");
        }
        //先删除原来的权限
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id",id);

        roleMenuMapper.delete(roleMenuQueryWrapper);
        //增加现在分配的角色
        if(mids.length>0){
            for (Long mid : mids) {
                Menu menu = menuMapper.selectById(mid);
                if(menu==null){
                    throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"menuId="+mid+",菜单权限不存在");
                }else {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(id);
                    roleMenu.setMenuId(mid);
                    roleMenuMapper.insert(roleMenu);
                }
            }
        }
    }

}
