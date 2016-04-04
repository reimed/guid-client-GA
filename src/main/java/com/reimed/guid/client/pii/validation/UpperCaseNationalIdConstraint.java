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

import com.reimed.guid.client.pii.NationalId;

import lombok.NonNull;
import lombok.Value;

@Value
public class UpperCaseNationalIdConstraint
    implements FactorConstraint<NationalId> {

  @Override
  public Class<NationalId> getTargetType() {
    return NationalId.class;
  }

  @Override
  public FactorConstraintError apply(@NonNull NationalId value) {
    if (value.getNationalId().trim().isEmpty()) {
      return new FactorConstraintError(value.getClass(),
          "National Id cannot be empty");
    }

    if (!value.getNationalId().trim().equals(value.getNationalId())) {
      return new FactorConstraintError(value.getClass(),
          "National Id hasn't been trimmed");
    }

    if (!value.getNationalId().toUpperCase().equals(value.getNationalId())) {
      return new FactorConstraintError(value.getClass(),
          "National Id hasn't been upcased");
    }

    return null;
  }

}
