package com.mercateo.rest.jersey.utils.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mercateo.rest.jersey.utils.validation.EnumValue;
import com.mercateo.rest.jersey.utils.validation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.Collection;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ValidationError {

    @NonNull
    ValidationErrorCode code;

    @NonNull
    String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer limit;

    public static ValidationError of(ConstraintViolation<?> constraintViolation){
        Annotation annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
        String path = constructJsonPath(constraintViolation.getPropertyPath());

        ValidationError error = new ValidationError(ValidationErrorCode.UNKNOWN, path);

        if(annotation instanceof NotNull || annotation instanceof NotBlank || annotation instanceof AssertTrue){
            return new ValidationError(ValidationErrorCode.REQUIRED, path);
        } else if(annotation instanceof Size){
            final Size sizeAnnotation = (Size) annotation;
            final Object value = constraintViolation.getInvalidValue();

            if (value instanceof String && value.toString().length() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINLENGTH, path, sizeAnnotation.min());
            } else if (value instanceof String && value.toString().length() > sizeAnnotation.max()) {
                error = new ValidationError(ValidationErrorCode.MAXLENGTH, path, sizeAnnotation.max());
            } else if (value instanceof Collection && ((Collection<?>) value).size() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINITEMS, path, sizeAnnotation.min());
            } else if (value instanceof Collection && ((Collection<?>) value).size() > sizeAnnotation.max()) {
                error = new ValidationError(ValidationErrorCode.MAXITEMS, path, sizeAnnotation.max());
            }
        } else if(annotation instanceof EnumValue){
            error = new ValidationError(ValidationErrorCode.ENUM, path);
        } else if(annotation instanceof NullOrNotBlank){
            error = new ValidationError(ValidationErrorCode.INVALID, path);
        } else if (annotation instanceof Min) {
            final Min min = (Min) annotation;
            error = new ValidationError(ValidationErrorCode.MINLENGTH, path, ((int) min.value()));
        } else if (annotation instanceof Max) {
            Max max = (Max) annotation;
            error = new ValidationError(ValidationErrorCode.MAXLENGTH, path, ((int) max.value()));
        } else if (annotation instanceof Email) {
            error = new ValidationError(ValidationErrorCode.INVALID_EMAIL, path);
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
