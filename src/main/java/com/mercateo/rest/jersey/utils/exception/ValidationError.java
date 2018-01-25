package com.mercateo.rest.jersey.utils.exception;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mercateo.rest.jersey.utils.validation.EnumValue;
import com.mercateo.rest.jersey.utils.validation.NullOrNotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ValidationError {

    @NonNull
    String code;

    @NonNull
    String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer limit;

    public static ValidationError of(ConstraintViolation<?> constraintViolation) {
        Annotation annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
        String path = constructJsonPath(constraintViolation.getPropertyPath());
        ValidationError error = null;

        if (annotation instanceof NotNull || annotation instanceof NotBlank || annotation instanceof AssertTrue) {
            return new ValidationError(ValidationErrorCode.REQUIRED.name(), path);
        } else if (annotation instanceof Size) {
            final Size sizeAnnotation = (Size) annotation;
            final Object value = constraintViolation.getInvalidValue();

            if (value instanceof String && value.toString().length() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINLENGTH.name(), path, sizeAnnotation.min());
            } else if (value instanceof String && value.toString().length() > sizeAnnotation.max()) {
                error = new ValidationError(ValidationErrorCode.MAXLENGTH.name(), path, sizeAnnotation.max());
            } else if (value instanceof Collection && ((Collection<?>) value).size() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINITEMS.name(), path, sizeAnnotation.min());
            } else if (value instanceof Collection && ((Collection<?>) value).size() > sizeAnnotation.max()) {
                error = new ValidationError(ValidationErrorCode.MAXITEMS.name(), path, sizeAnnotation.max());
            }
        } else if (annotation instanceof Min) {
            final Min min = (Min) annotation;
            error = new ValidationError(ValidationErrorCode.MINLENGTH.name(), path, ((int) min.value()));
        } else if (annotation instanceof Max) {
            Max max = (Max) annotation;
            error = new ValidationError(ValidationErrorCode.MAXLENGTH.name(), path, ((int) max.value()));
        } else if (annotation instanceof Email) {
            error = new ValidationError(ValidationErrorCode.INVALID_EMAIL.name(), path);
        } else {
            String code = constraintViolation.getMessage() != null && !constraintViolation.getMessage().isEmpty()
                    ? constraintViolation.getMessage()
                    : ValidationErrorCode.UNKNOWN.name();
            error = new ValidationError(code, path);
        }
        return error;
    }

    private static String constructJsonPath(Path path) {
        StringBuilder jsonPath = new StringBuilder("#");
        path.forEach(pathComponent -> {
            if (pathComponent.getKind() == ElementKind.PROPERTY) {
                jsonPath.append("/").append(pathComponent.getName());
            }
        });
        return jsonPath.toString();
    }
}
