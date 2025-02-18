package com.shq.oper.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spel表达式工具类
 * 
 * @author tjun
 */
public class SpelUtils {
	private static ExpressionParser parser = new SpelExpressionParser();
	private static ParserContext parserContext = new TemplateParserContext("{", "}");
	
	public static EvaluationContext newEvaluationContext(){
		return new StandardEvaluationContext();
	}
	public static Expression parseExpression(String expressionString){
		return parser.parseExpression(expressionString, parserContext);
	}
	public static <T> T getValue(EvaluationContext context, String expressionString, Class<T> desiredResultType){
		return parseExpression(expressionString).getValue(context, desiredResultType);
	}
	public static String getValue(EvaluationContext context, String expressionString){
		return getValue(context, expressionString, String.class);
	}
}
