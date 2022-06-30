package com.erahub.system.service;

import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Menu;
import com.erahub.common.model.system.Role;
import com.erahub.common.model.system.User;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.system.*;

import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2020/3/7 15:43
 * @Version 1.0
 **/
public interface UserService {

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return
     */
     User findUserByName(String name);

    /**
     * 查询用户角色
     *
     * @param id 用户id
     * @return
     */
     List<Role> findRolesById(Long id) throws SystemException;

    /**
     * 根据用户角色查询用户的菜单
     * 菜单: menu+button
     *
     * @param roles 用户的角色
     * @return
     */
    List<Menu> findMenuByRoles(List<Role> roles);

    /**
     * 加载菜单
     *
     * @return
     */
    List<MenuNodeVO> findMenu();

    /**
     * 用户列表
     * @param userVO
     * @return
     */
    PageVO<UserVO> findUserList(Integer pageNum, Integer pageSize, UserVO userVO);

    /**
     * 删除用户
     *
     * @param id
     */
    void deleteById(Long id) throws SystemException;

    /**
     * 更新状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Boolean status) throws SystemException;

    /**
     * 添加用户
     * @param userVO
     */
    void add(UserVO userVO) throws SystemException;

    /**
     * 修改用户密码
     * @param paramMap
     */
    void changeUserPassword(Map<String, Object> paramMap) throws SystemException;

    /**
     * 更新用户
     *
     * @param id
     * @param userVO
     */
    void update(Long id, UserEditVO userVO) throws SystemException;

    /**
     * 编辑用户
     *
     * @param id
     * @return
     */
    UserEditVO edit(Long id) throws SystemException;

    /**
     * 已拥有的角色ids
     *
     * @param id 用户id
     * @return
     */
    List<Long> roles(Long id) throws SystemException;

    /**
     * 分配角色
     *
     * @param id
     * @param rids
     */
    void assignRoles(Long id, Long[] rids) throws SystemException;

    /**
     * 所有用户
     *
     * @return
     */
    List<User> findAll();

    /**
     * 用户登入
     *
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password) throws SystemException;


    /**
     * 用户详情
     *
     * @return
     */
    UserInfoVO info() throws SystemException;

}
