/*
 * Copyright © 2017 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
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

        if (annotation instanceof NotNull || annotation.annotationType().getSimpleName().equals("NotBlank") || annotation instanceof AssertTrue) {
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
        } else if (annotation.annotationType().getSimpleName().equals("Email")) {
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
