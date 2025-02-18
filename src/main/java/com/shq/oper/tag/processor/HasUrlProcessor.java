package com.shq.oper.tag.processor;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.util.Constant;

/**
 * url权限控制
 * @author tjun
 */
public class HasUrlProcessor extends AbstractStandardConditionalVisibilityTagProcessor {
	public static final int ATTR_PRECEDENCE = 300;
	public static final String ATTR_NAME = "has-url";

	public HasUrlProcessor(final TemplateMode templateMode, final String dialectPrefix) {
		super(templateMode, dialectPrefix, ATTR_NAME, ATTR_PRECEDENCE);
	}

	@Override
	protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue) {
		if (!(context instanceof IWebContext)) {
			throw new ConfigurationException("Thymeleaf execution context is not a web context (implementation of " + IWebContext.class.getName()
					+ "). my:has-url can only be used in " + "web environments.");
		}
		final IWebContext webContext = (IWebContext) context;
		final HttpSession session = webContext.getSession();

		AdminDto adminUser = (AdminDto) session.getAttribute(com.shq.oper.util.Constant.OPER_USER);
		List<Resource> resources = adminUser.getAllResources();
		// 已配置url权限控制，且未授权，元素不可见
		return resources.stream().anyMatch(p -> {
			return Constant.OPER_SYSTEM_CODE_VALUE.equals(p.getSystemCode()) 
					&& attributeValue.equals(p.getUrl()) 
					;
		});
	}

}
