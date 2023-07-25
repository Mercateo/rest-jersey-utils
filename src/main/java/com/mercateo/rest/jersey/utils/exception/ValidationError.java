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

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String pattern;

    public ValidationError(@NonNull String code, @NonNull String path, @NonNull Integer limit) {
        this.code = code;
        this.path = path;
        this.limit = limit;
    }

    public ValidationError(@NonNull String code, @NonNull String path, @NonNull String pattern) {
        this.code = code;
        this.path = path;
        this.pattern = pattern;
    }

    @SuppressWarnings("boxing")
    public static ValidationError of(ConstraintViolation<?> constraintViolation) {
        Annotation annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
        String path = constructJsonPath(constraintViolation.getPropertyPath());
        ValidationError error = null;

        if (annotation instanceof NotNull || annotation.annotationType().getSimpleName().equals(
                "NotBlank") || annotation instanceof AssertTrue) {
            return new ValidationError(ValidationErrorCode.REQUIRED.name(), path);
        } else if (annotation instanceof Size) {
            final Size sizeAnnotation = (Size) annotation;
            final Object value = constraintViolation.getInvalidValue();

            if (value instanceof String && value.toString().length() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINLENGTH.name(), path,
                        sizeAnnotation.min());
            } else if (value instanceof String && value.toString().length() > sizeAnnotation
                    .max()) {
                error = new ValidationError(ValidationErrorCode.MAXLENGTH.name(), path,
                        sizeAnnotation.max());
            } else if (value instanceof Collection && ((Collection<?>) value)
                    .size() < sizeAnnotation.min()) {
                error = new ValidationError(ValidationErrorCode.MINITEMS.name(), path,
                        sizeAnnotation.min());
            } else if (value instanceof Collection && ((Collection<?>) value)
                    .size() > sizeAnnotation.max()) {
                error = new ValidationError(ValidationErrorCode.MAXITEMS.name(), path,
                        sizeAnnotation.max());
            }
        } else if (annotation instanceof Min) {
            final Min min = (Min) annotation;
            error = new ValidationError(ValidationErrorCode.MINLENGTH.name(), path, ((int) min
                    .value()));
        } else if (annotation instanceof Max) {
            Max max = (Max) annotation;
            error = new ValidationError(ValidationErrorCode.MAXLENGTH.name(), path, ((int) max
                    .value()));
        } else if (annotation.annotationType().getSimpleName().equals("Email")) {
            error = new ValidationError(ValidationErrorCode.INVALID_EMAIL.name(), path);
        } else if (annotation instanceof Pattern) {
            final Pattern pattern = (Pattern) annotation;
            error = new ValidationError(ValidationErrorCode.PATTERN.name(), path, pattern
                    .regexp());
        } else {
            String code = constraintViolation.getMessage() != null && !constraintViolation
                    .getMessage().isEmpty()
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
                jsonPath.append("/").append(pathComponent);
            }
        });
        return jsonPath.toString();
    }
}
