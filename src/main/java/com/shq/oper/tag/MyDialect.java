package com.shq.oper.tag;

import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import com.shq.oper.tag.processor.HasUrlProcessor;

/**
 * 自定义thymeleaf标签
 * @author tjun
 */
public class MyDialect extends AbstractProcessorDialect {
	private static final String NAME = "My";
	private static final String PREFIX = "my";

	public MyDialect() {
		super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		return createStandardProcessorsSet(dialectPrefix);
	}

	private Set<IProcessor> createStandardProcessorsSet(String dialectPrefix) {
		LinkedHashSet<IProcessor> processors = new LinkedHashSet<IProcessor>();

        final TemplateMode[] templateModes =
                new TemplateMode[] {
                        TemplateMode.HTML, TemplateMode.XML,
                        TemplateMode.TEXT, TemplateMode.JAVASCRIPT, TemplateMode.CSS };

        for (final TemplateMode templateMode : templateModes) {
            processors.add(new HasUrlProcessor(templateMode, dialectPrefix));
        }
		return processors;
	}
}
