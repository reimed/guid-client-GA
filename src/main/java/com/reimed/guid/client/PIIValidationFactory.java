/*
 *
 * Copyright 2016 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.reimed.guid.client;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.reimed.guid.client.pii.Birthday;
import com.reimed.guid.client.pii.Birthplace;
import com.reimed.guid.client.pii.Name;
import com.reimed.guid.client.pii.NationalId;
import com.reimed.guid.client.pii.PII;
import com.reimed.guid.client.pii.validation.GreogorianBirthdayValidator;
import com.reimed.guid.client.pii.validation.UpperCaseBirthplaceValidator;
import com.reimed.guid.client.pii.validation.UpperCaseNameValidator;
import com.reimed.guid.client.pii.validation.UpperCaseNationalIdValidator;
import com.reimed.guid.client.pii.validation.ValidationError;
import com.reimed.guid.client.pii.validation.Validator;

import lombok.NonNull;

public final class PIIValidationFactory {

  private PIIValidationFactory() {}

  private static Map<Class<?>, Validator<?>> validators = newHashMap();

  static {
    validators.put(Name.class, new UpperCaseNameValidator());
    validators.put(Birthday.class, new GreogorianBirthdayValidator());
    validators.put(Birthplace.class, new UpperCaseBirthplaceValidator());
    validators.put(NationalId.class, new UpperCaseNationalIdValidator());
  }

  public static <T> void putValidator(Class<T> type, Validator<T> validator) {
    validators.put(type, validator);
  }

  public static <T> void getValidator(Class<T> type) {
    validators.get(type);
  }

  public static void clearAllValidators() {
    validators.clear();
  }

  public static void validate(@NonNull PII pii) {
    for (Class<?> type : validators.keySet()) {
      if (type.isAssignableFrom(pii.getClass())) {
        @SuppressWarnings("unchecked")
        Validator<Object> validator = (Validator<Object>) validators.get(type);
        ValidationError error = validator.validate((Object) pii);
        if (error != null) throw new IllegalStateException(
            error.getType().getName() + ": " + error.getMessage());
      }
    }
  }

}
