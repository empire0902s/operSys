package com.shq.oper.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.shq.oper.model.domain.primarydb.Resource;

import lombok.Data;


/**
 * 菜单Dto
 *
 */
@Data
public class MenuTreeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Long id;
	private Long checkboxValue;
	private String icon;
	private Integer sort;
	private Integer menuLevel;
	private Integer isIntercept;
 	private Long parentId;
 	private Long parentName;
	
 	private Boolean checked;
	private List<MenuTreeDto> children;


	
	public static MenuTreeDto initByMenu(Resource entity,List<Long>listAdminMenuIds) {
		MenuTreeDto _menuTree = new MenuTreeDto();
		_menuTree.setName(entity.getName());
		_menuTree.setId(entity.getId());
		_menuTree.setCheckboxValue(entity.getId());
		_menuTree.setIcon(entity.getIcon());
		_menuTree.setSort(entity.getSort()==null?100:entity.getSort().intValue());
		_menuTree.setMenuLevel(entity.getType().intValue());
		_menuTree.setParentId(entity.getParentId());
		// 选中
		_menuTree.setChecked(listAdminMenuIds.contains(entity.getId()));
		return _menuTree;
	}

	public static List<MenuTreeDto> toListMenuTree(List<Resource> listMenuAll,List<Long> listRolesMenuIds) {
		
		List<MenuTreeDto> menusListTmp= new ArrayList<MenuTreeDto>();	//菜单集合转换
		 // 最后的结果
	    List<MenuTreeDto> parentList = new ArrayList<MenuTreeDto>();
	    // 先找到所有的一级菜单
	    for (Resource _menuTmp : listMenuAll) {
	    	Long parentId = _menuTmp.getParentId();
	    	MenuTreeDto _treeDto = MenuTreeDto.initByMenu(_menuTmp, listRolesMenuIds);
	    	menusListTmp.add(_treeDto);
	        // 一级菜单没有parentId  或者 父节点 < 0
	        if (StringUtils.isEmpty(parentId) || parentId < 0) {
	            parentList.add(_treeDto);
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (MenuTreeDto parentDto : parentList) {
	    	parentDto.setChildren(getChild(parentDto.getId(), menusListTmp));
	    }
		
		return parentList;
	}
	
	/**
	 * 递归查找子菜单
	 * @param id  当前菜单id
	 * @param listMenuAll 要查找的列表
	 * @return
	 */
	private static List<MenuTreeDto> getChild(Long id, List<MenuTreeDto> listMenuAll) {
	    // 子菜单
	    List<MenuTreeDto> childList = new ArrayList<>();
	    for (MenuTreeDto _menuDto : listMenuAll) {
	    	Long _parentId = _menuDto.getParentId();
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (!StringUtils.isEmpty(_parentId)) {
	            if (id.equals(_parentId)) {
	                childList.add(_menuDto);
	            }
	        }
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (MenuTreeDto menuChild : childList) {
	            // 递归
	            menuChild.setChildren(getChild(menuChild.getId(), listMenuAll));
	    } // 递归退出条件
	    if (childList.size() == 0) {
	        return null;
	    }
	    return childList;
	}
}
