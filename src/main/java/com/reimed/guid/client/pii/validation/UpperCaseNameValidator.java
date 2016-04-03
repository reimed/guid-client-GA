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
package com.reimed.guid.client.pii.validation;

import com.reimed.guid.client.pii.Name;

import lombok.NonNull;
import lombok.Value;

@Value
public class UpperCaseNameValidator implements Validator<Name> {

  @Override
  public ValidationError validate(@NonNull Name value) {
    if (value.getSurname().trim().isEmpty()) {
      return new ValidationError(Name.class, "Surname cannot be empty");
    }

    if (value.getGivenName().trim().isEmpty()) {
      return new ValidationError(Name.class, "Given name cannot be empty");
    }

    if (!value.getSurname().trim().equals(value.getSurname())) {
      return new ValidationError(Name.class, "Surname hasn't been trimmed");
    }

    if (!value.getGivenName().trim().equals(value.getGivenName())) {
      return new ValidationError(Name.class, "Given name hasn't been trimmed");
    }

    if (!value.getSurname().toUpperCase().equals(value.getSurname())) {
      return new ValidationError(Name.class, "Surname hasn't been upcased");
    }

    if (!value.getGivenName().toUpperCase().equals(value.getGivenName())) {
      return new ValidationError(Name.class, "Given name hasn't been upcased");
    }

    return null;
  }

}
