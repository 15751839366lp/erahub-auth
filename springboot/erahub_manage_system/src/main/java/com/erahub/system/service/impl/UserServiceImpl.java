package com.erahub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.common.enums.system.UserStatusEnum;
import com.erahub.common.enums.system.UserTypeEnum;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.*;
import com.erahub.common.response.ActiveUser;
import com.erahub.common.utils.JWTUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.system.util.MenuTreeBuilder;
import com.erahub.system.converter.MenuConverter;
import com.erahub.system.converter.UserConverter;
import com.erahub.system.mapper.*;
import com.erahub.system.service.DepartmentService;
import com.erahub.system.service.UserService;
import com.erahub.system.shiro.JWTToken;
import com.erahub.system.util.MD5Utils;
import com.erahub.common.vo.system.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lipeng
 * @Date 2020/3/7 15:44
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询用户
     *
     * @param name 用户名
     * @return
     */
    @Override
    public User findUserByName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",name);
        return userMapper.selectOne(userQueryWrapper);
    }

    /**
     * 查询用户角色
     *
     * @param id 用户ID
     * @return
     */
    @Override
    public List<Role> findRolesById(Long id) throws SystemException {
        User dbUser = userMapper.selectById(id);
        if (dbUser == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该用户不存在");
        }
        List<Role> roles = new ArrayList<>();
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", dbUser.getId());

        List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        List<Long> rids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleList)) {
            for (UserRole userRole : userRoleList) {
                rids.add(userRole.getRoleId());
            }
            if (!CollectionUtils.isEmpty(rids)) {
                for (Long rid : rids) {
                    Role role = roleMapper.selectById(rid);
                    if (role != null) {
                        roles.add(role);
                    }
                }
            }
        }
        return roles;
    }

    /**
     * 查询权限
     *
     * @param roles 用户的角色
     * @return
     */
    @Override
    public List<Menu> findMenuByRoles(List<Role> roles) {
        List<Menu> menus = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            Set<Long> menuIds = new HashSet<>();//存放用户的菜单id
            List<RoleMenu> roleMenus;
            for (Role role : roles) {
                //根据角色ID查询权限ID
                QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
                roleMenuQueryWrapper.eq("role_id", role.getId());

                roleMenus = roleMenuMapper.selectList(roleMenuQueryWrapper);
                if (!CollectionUtils.isEmpty(roleMenus)) {
                    for (RoleMenu roleMenu : roleMenus) {
                        menuIds.add(roleMenu.getMenuId());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(menuIds)) {
                for (Long menuId : menuIds) {
                    //该用户所有的菜单
                    Menu menu = menuMapper.selectById(menuId);
                    if (menu != null) {
                        menus.add(menu);
                    }
                }
            }
        }
        return menus;
    }

    /**
     * 获取菜单
     *
     * @return
     */
    @Override
    public List<MenuNodeVO> findMenu() {
        List<Menu> menus = null;
        List<MenuNodeVO> menuNodeVOS = new ArrayList<>();
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode()) {
            //超级管理员
            menus = menuMapper.selectList(null);
        } else if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_USER.getTypeCode()) {
            //普通系统用户
            menus = activeUser.getMenus();
        }
        if (!CollectionUtils.isEmpty(menus)) {
            menuNodeVOS = MenuConverter.converterToMenuNodeVO(menus);
        }
        //构建树形菜单
        return MenuTreeBuilder.build(menuNodeVOS);
    }

    /**
     * 用户列表
     *
     * @param userVO
     * @return
     */
    @Override
    public PageVO<UserVO> findUserList(Integer pageNum, Integer pageSize, UserVO userVO) {
        //已经拥有的
        IPage<User> userIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        String username = userVO.getUsername();
        String realname = userVO.getRealname();
        Long departmentId = userVO.getDepartmentId();
        Integer sex = userVO.getSex();
        String email = userVO.getEmail();

        if (username != null && !"".equals(username)) {
            userQueryWrapper.like("username", username);
        }
        if (realname != null && !"".equals(realname)) {
            userQueryWrapper.like("realname", realname);
        }
        if (email != null && !"".equals(email)) {
            userQueryWrapper.like("email", email);
        }
        if (sex != null) {
            userQueryWrapper.eq("sex", sex);
        }
        if (departmentId != null) {
            userQueryWrapper.eq("department_id", departmentId);
        }
        userQueryWrapper.ne("type", 0);

        userIPage = userMapper.selectPage(userIPage, userQueryWrapper);
        List<User> userList = userIPage.getRecords();
        List<UserVO> userVOS = userConverter.converterToUserVOList(userList);

        return new PageVO<>(userIPage.getTotal(), userVOS);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @Transactional
    @Override
    public void deleteById(Long id) throws SystemException {
        User user = userMapper.selectById(id);
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

        if (user == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要删除的用户不存在");
        }

        if (user.getId().equals(activeUser.getUser().getId())) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "不能删除当前登入用户");
        }

        userMapper.deleteById(id);
        //删除对应[用户-角色]记录
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", id);

        userRoleMapper.delete(userRoleQueryWrapper);
    }

    /**
     * 更新用户禁用状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) throws SystemException {
        User dbUser = userMapper.selectById(id);
        if (dbUser == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要更新状态的用户不存在");
        }
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (dbUser.getId().equals(activeUser.getUser().getId())) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "无法改变当前用户状态");
        } else {
            User t = new User();
            t.setId(id);
            t.setStatus(status ? UserStatusEnum.DISABLE.getStatusCode() :
                    UserStatusEnum.AVAILABLE.getStatusCode());
            userMapper.updateById(t);
        }
    }

    /**
     * 添加用户
     *
     * @param userVO
     */
    @Transactional
    @Override
    public void add(UserVO userVO) throws SystemException {
        @NotBlank(message = "用户名不能为空") String username = userVO.getUsername();
        @NotNull(message = "部门id不能为空") Long departmentId = userVO.getDepartmentId();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);

        int i = userMapper.selectCount(userQueryWrapper);
        if (i != 0) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该用户名已被占用");
        }
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该部门不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        String salt = UUID.randomUUID().toString().substring(0, 32);
        user.setPassword(MD5Utils.md5Encryption(user.getPassword(), salt));
        user.setModifiedTime(new Date());
        user.setCreateTime(new Date());
        user.setSalt(salt);
        user.setType(UserTypeEnum.SYSTEM_USER.getTypeCode());//添加的用户默认是普通用户
        user.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());//添加的用户默认启用
        user.setAvatar("../assets/test01.webp");
        userMapper.insert(user);
    }

    /**
     * 修改用户密码
     *
     * @param paramMap
     */
    @Transactional
    @Override
    public void changeUserPassword(Map<String, Object> paramMap) throws SystemException {
        User user = userMapper.selectById((int)paramMap.get("id"));

        if(user == null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要修改的用户不存在");
        }

        String salt = UUID.randomUUID().toString().substring(0, 32);
        user.setSalt(salt);
        user.setPassword(MD5Utils.md5Encryption((String)paramMap.get("password"), salt));

        user.setModifiedTime(new Date());

        userMapper.updateById(user);
    }

    /**
     * 更新
     *
     * @param id
     * @param userVO
     */
    @Transactional
    @Override
    public void update(Long id, UserEditVO userVO) throws SystemException {
        User dbUser = userMapper.selectById(id);
        @NotBlank(message = "用户名不能为空") String username = userVO.getUsername();
        @NotNull(message = "部门不能为空") Long departmentId = userVO.getDepartmentId();
        if (dbUser == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要修改的用户不存在");
        }
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该部门不存在");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);

        List<User> users = userMapper.selectList(userQueryWrapper);
        if (!CollectionUtils.isEmpty(users)) {
            if (!users.get(0).getId().equals(id)) {
                throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该用户名已被占用");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setModifiedTime(new Date());
        user.setId(dbUser.getId());
        userMapper.updateById(user);
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public UserEditVO edit(Long id) throws SystemException {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要编辑的用户不存在");
        }
        UserEditVO userEditVO = new UserEditVO();
        BeanUtils.copyProperties(user, userEditVO);
        Department department = departmentMapper.selectById(user.getDepartmentId());
        if (department != null) {
            userEditVO.setDepartmentId(department.getId());
        }
        return userEditVO;
    }

    /**
     * 用户拥有的角色ID
     *
     * @param id 用户id
     * @return
     */
    @Transactional
    @Override
    public List<Long> roles(Long id) throws SystemException {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该用户不存在");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());

        List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        List<Long> roleIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleList)) {
            for (UserRole userRole : userRoleList) {
                Role role = roleMapper.selectById(userRole.getRoleId());
                if (role != null) {
                    roleIds.add(role.getId());
                }
            }
        }
        return roleIds;
    }

    /**
     * 分配角色
     *
     * @param id   用户id
     * @param rids 角色数组
     */
    @Override
    @Transactional
    public void assignRoles(Long id, Long[] rids) throws SystemException {
        //删除之前用户的所有角色
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "用户不存在");
        }
        //删除之前分配的
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());

        userRoleMapper.delete(userRoleQueryWrapper);
        //增加现在分配的
        if (rids.length > 0) {
            for (Long rid : rids) {
                Role role = roleMapper.selectById(rid);
                if (role == null) {
                    throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "roleId=" + rid + ",该角色不存在");
                }
                //判断角色状态
                if (role.getStatus() == 0) {
                    throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "roleName=" + role.getRoleName() + ",该角色已禁用");
                }
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(rid);
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    /**
     * 用户登入
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) throws SystemException {
        String token;
        User user = findUserByName(username);
        if (user != null) {
            String salt = user.getSalt();
            //秘钥为盐
            String target = MD5Utils.md5Encryption(password, salt);
            //生成Token
            token = JWTUtils.sign(username, target);
            JWTToken jwtToken = new JWTToken(token);
            try {
                SecurityUtils.getSubject().login(jwtToken);
            } catch (AuthenticationException e) {
                throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, e.getMessage());
            }
        } else {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "用户不存在");
        }
        return token;
    }

    /**
     * 用户详情
     *
     * @return
     */
    @Override
    public UserInfoVO info() throws SystemException {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setAvatar(activeUser.getUser().getAvatar());
        userInfoVO.setUsername(activeUser.getUser().getUsername());
        userInfoVO.setUrl(activeUser.getUrls());
        userInfoVO.setRealname(activeUser.getUser().getRealname());
        List<String> roleNames = activeUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        userInfoVO.setRoles(roleNames);
        userInfoVO.setPerms(activeUser.getPermissions());
        userInfoVO.setIsAdmin(activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode());
        DepartmentVO dept = departmentService.edit(activeUser.getUser().getDepartmentId());
        if (dept != null) {
            userInfoVO.setDepartment(dept.getName());
        }
        return userInfoVO;
    }
}
