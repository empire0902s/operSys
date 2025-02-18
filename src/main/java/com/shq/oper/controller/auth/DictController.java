package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.DictMapper;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.DictService;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据字典
 * 
 * @author Yangxianxing
 *
 */
@RestController
@Slf4j
@RequestMapping("/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@Autowired
	private DictMapper dictMapper;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Dict> list(Dict entity, PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<Dict> entitys = dictMapper.queryLikeDict(entity);
		return Data.ok(entitys);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Dict dict) {
		LocalDateTime dtNow = LocalDateTime.now();
		dict.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		dict.setUpdateTime(dtNow);
		if (ObjectUtils.isEmpty(dict.getId())) {
			Dict tmp = new Dict();
			tmp.setCode(dict.getCode());
			List<Dict> dictList = dictService.select(tmp);
			if (dictList.size()>0) {
				for (Dict tmpDict : dictList) {
					if (tmpDict.getDictValue().equals(dict.getDictValue())) {
						return Msg.error("值[" + dict.getDictValue() + "]已存在，请修改后重新保存");
					}
					if (tmpDict.getDictKey().equals(dict.getDictKey())) {
						return Msg.error("值[" + dict.getDictKey() + "]已存在，请修改后重新保存");
					}
				}
			}
			dict.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			dict.setCreateTime(dtNow);
			dictService.insert(dict);
		} else {
			Dict tmpDict = new Dict();
			tmpDict.setCode(dict.getCode());
			List<Dict> dictList = dictService.select(tmpDict);
			if (dictList.size()>0) {
				for (Dict tmp : dictList) {
					if (tmp.getDictKey().equals(dict.getDictKey())) {
						if (!tmp.getId().equals(dict.getId())) {
							return Msg.error("值[" + dict.getDictKey() + "]已存在，请修改后重新保存");
						}
					}
					if (tmp.getDictValue().equals(dict.getDictValue())) {
						if (!tmp.getId().equals(dict.getId())) {
							return Msg.error("值[" + dict.getDictValue() + "]已存在，请修改后重新保存");
						}
					}
				}
			}
			dictService.updateDict(dict);
		}
		return Msg.ok("保存成功");
	}

	@RequestMapping("/dictDetails")
	public ModelAndView couponsAreaRuleDetails(HttpServletRequest request, Dict dict) throws Exception {
		Dict dictDetails = dictService.selectOne(dict);
		request.setAttribute("dictDetails", dictDetails);
		return toPage(request);
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Dict dict) {
		dict.setIsDisabled(true);
		dict.setUpdateTime(LocalDateTime.now());
		dict.setUpdateAdmin(dict.getCreateAdmin());
		dictService.disabledById(dict);
		return Msg.ok("禁用成功");
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Dict dict) {
		LocalDateTime dtNow = LocalDateTime.now();
		dict.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		dict.setUpdateTime(dtNow);
		dictService.enableById(dict);
		return Msg.ok("启用成功");
	}

	@GetMapping(value = "/selectValue")
	public List<SelectValueData<String>> dict(Dict entity, boolean isCode) {
		List<Dict> datas = dictService.select(entity);
		List<SelectValueData<String>> list = datas.stream()
				.map(d -> new SelectValueData<String>(d.getDictKey(), d.getDictValue()))
				.collect(Collectors.toList());
		return list;
	}
	@GetMapping(value = "/selectBannerIndexValue")
	public List<SelectValueData<Long>> selectValue(Dict entity, boolean isCode) {
		List<Dict> datas = dictMapper.queryIndexBannerOfDict();
		List<SelectValueData<Long>> list = datas.stream()
				.map(d -> new SelectValueData<Long>(d.getName(), d.getId()))
				.collect(Collectors.toList());
		return list;
	}

}
