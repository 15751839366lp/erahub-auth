package com.erahub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Menu;
import com.erahub.common.model.system.RoleMenu;
import com.erahub.system.mapper.RoleMenuMapper;
import com.erahub.system.util.MenuTreeBuilder;
import com.erahub.common.vo.system.MenuNodeVO;
import com.erahub.common.vo.system.MenuVO;
import com.erahub.system.converter.MenuConverter;
import com.erahub.system.mapper.MenuMapper;
import com.erahub.system.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author lipeng
 * @Date 2020/3/10 11:56
 * @Version 1.0
 **/
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 加载菜单树（按钮和菜单）
     *
     * @return
     */
    @Override
    public List<MenuNodeVO> findMenuTree(Boolean isAuthority) {
        List<Menu> menus = menuMapper.selectList(null);
        List<MenuNodeVO> menuNodeVOS = MenuConverter.converterToALLMenuNodeVO(menus);

        List<MenuNodeVO> tree = MenuTreeBuilder.build(menuNodeVOS);

        if(!isAuthority){

            //无父节点的菜单按钮
            MenuNodeVO noParentMenu = new MenuNodeVO();
            List<MenuNodeVO> noParentMenuList = new ArrayList<>();
            //父节点的id
            Set<Long> parentIdSet = new HashSet<>();
            parentIdSet.add(0l);

            for (MenuNodeVO nav : menuNodeVOS) {
                parentIdSet.add(nav.getId());
            }

            for (MenuNodeVO nav : menuNodeVOS) {
                if(!parentIdSet.contains(nav.getParentId())){
                    noParentMenuList.add(nav);
                }
            }

            if(noParentMenuList.size() > 0){
                noParentMenu.setChildren(noParentMenuList);
                noParentMenu.setMenuName("其他");
                noParentMenu.setIcon("el-icon-s-grid");
                noParentMenu.setType(0);
                noParentMenu.setOpen(1);
                tree.add(noParentMenu);
            }
        }

        return tree;
    }

    /**
     * 添加菜单
     *
     * @param menuVO
     */
    @Override
    public Menu add(MenuVO menuVO) throws SystemException{
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("menu_name", menuVO.getMenuName());
        wrapper.eq("type", menuVO.getType());

        int i = menuMapper.selectCount(wrapper);
        if (i != 0) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该菜单名已被占用");
        }

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVO,menu);
        menu.setCreateTime(new Date());
        menu.setModifiedTime(new Date());
        menu.setAvailable(menuVO.isDisabled()?0:1);
        menuMapper.insert(menu);
        return menu;
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void delete(Long id) throws SystemException {
        Menu menu = menuMapper.selectById(id);
        if(menu==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要删除的菜单不存在");
        }
        menuMapper.deleteById(id);

        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("menu_id",id);
        roleMenuMapper.delete(roleMenuQueryWrapper);
    }

    /**
     * 编辑菜单
     * @param id
     * @return
     */
    @Override
    public MenuVO edit(Long id) throws SystemException {
        Menu menu = menuMapper.selectById(id);
        if(menu==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"该编辑的菜单不存在");
        }
        return MenuConverter.converterToMenuVO(menu);
    }

    /**
     * 更新菜单
     * @param id
     * @param menuVO
     */
    @Override
    public void update(Long id, MenuVO menuVO) throws SystemException {
        Menu dbMenu = menuMapper.selectById(id);
        if(dbMenu==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要更新的菜单不存在");
        }

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("menu_name", menuVO.getMenuName());
        wrapper.eq("type", menuVO.getType());

        int i = menuMapper.selectCount(wrapper);
        if (!dbMenu.getMenuName().equals(menuVO.getMenuName()) && i != 0) {
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "该菜单名已被占用");
        }

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVO,menu);
        menu.setId(id);
        menu.setAvailable(menuVO.isDisabled()?0:1);
        menu.setModifiedTime(new Date());
        menuMapper.updateById(menu);
    }

    /**
     * 获取展开项
     *
     * @return
     */
    @Override
    public List<Long> findOpenIds() {
        List<Long> ids=new ArrayList<>();
        List<Menu> menus = menuMapper.selectList(null);
        if(!CollectionUtils.isEmpty(menus)){
            for (Menu menu : menus) {
                if(menu.getOpen()==1){
                    ids.add(menu.getId());
                }
            }
        }
        return ids;
    }



    /**
     * 获取所有菜单
     * @return
     */
    @Override
    public List<Menu> findAll() {
        return menuMapper.selectList(null);
    }


}
