package com.chuca.memberservice.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, Object> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // null일 경우 검증을 패스 (유효성 검사를 하는 다른 애노테이션으로 처리할 수 있도록)
        if (value == null) {
            return true;
        }

        // enumClass에서 Enum 값들을 가져옴
        Object[] enumValues = annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            // Enum 또는 String 타입 검증
            for (Object enumValue : enumValues) {
                if (value instanceof String) {
                    // String 값일 경우 대소문자 구분 또는 무시 설정에 따라 검증
                    if (((String) value).equals(enumValue.toString()) ||
                            (annotation.ignoreCase() && ((String) value).equalsIgnoreCase(enumValue.toString()))) {
                        return true;
                    }
                } else if (value.getClass().isEnum()) {
                    // Enum 타입일 경우 바로 비교
                    if (value == enumValue) {
                        return true;
                    }
                }
            }
        }

        return false; // 일치하는 값이 없으면 false 반환
    }
}
