package com.atinbo.webmvc.validator;


import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * 通用验证器
 *
 * @author breggor
 */
public class BeanValidatorUtils {

    /**
     * 存放错误信息的properties数组
     */
    public static final List<String> VALIDATION_MESSAGES = Arrays.asList("validationMessage");
    /**
     * 错误消息分隔符
     */
    public static final String SEPARATOR = "<br>";

    /**
     * 使用hibernate的注解来进行验证 .failFast(true)遇到错误立即报错
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(new AggregateResourceBundleLocator(VALIDATION_MESSAGES)))
            .buildValidatorFactory().getValidator();

    /**
     * 调用JSR303的validate方法, 验证失败时返回错误信息列表
     *
     * @param bean   要验证的JavaBean
     * @param groups 验证分组
     * @return 错误信息列表
     */
    public static <T> List<String> validateToList(T bean, Class<?>... groups) {
        List<String> errorMessages = new ArrayList<>();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean, groups);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> violation : constraintViolations) {
                errorMessages.add(violation.getMessage());
            }
        }
        return errorMessages;
    }

    /**
     * 调用JSR303的validate方法, 验证失败时返回错误信息Map
     *
     * @param bean   要验证的JavaBean
     * @param groups 验证分组
     * @return 错误信息列表
     */
    public static <T> Map<String, String> validateToMap(T bean, Class<?>... groups) {
        Map<String, String> errorMessages = new HashMap<String, String>();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean, groups);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> violation : constraintViolations) {
                errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return errorMessages;
    }

    /**
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     *
     * @param bean   要验证的JavaBean
     * @param groups 验证分组
     */
    public static void validateToException(Object bean, Class<?>... groups) throws ConstraintViolationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(bean, groups);
        if (constraintViolations.size() > 0) {
            // 错误信息以异常的 形式抛出可通过e.getConstraintViolations()方法获取具体验证信息
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 内部转换方法，将错误信息List转换成String
     *
     * @param errors 将错误信息List
     * @return 错误信息
     */
    public static String validateToString(List<String> errors) {
        StringBuffer strBuffer = new StringBuffer();
        for (String error : errors) {
            strBuffer.append(error);
            strBuffer.append(SEPARATOR);
        }
        return strBuffer.toString();
    }
}
