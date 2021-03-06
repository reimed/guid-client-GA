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

import com.reimed.guid.client.pii.Birthplace;

import lombok.NonNull;
import lombok.Value;

@Value
public class UpperCaseBirthplaceConstraint
    implements FactorConstraint<Birthplace> {

  @Override
  public Class<Birthplace> getTargetType() {
    return Birthplace.class;
  }

  @Override
  public FactorConstraintError apply(@NonNull Birthplace value) {
    if (value.getBirthplace().trim().isEmpty()) {
      return new FactorConstraintError(value.getClass(),
          "Birthplace cannot be empty");
    }

    if (!value.getBirthplace().trim().equals(value.getBirthplace())) {
      return new FactorConstraintError(value.getClass(),
          "Birthplace hasn't been trimmed");
    }

    if (!value.getBirthplace().toUpperCase().equals(value.getBirthplace())) {
      return new FactorConstraintError(value.getClass(),
          "Birthplace hasn't been upcased");
    }

    return null;
  }

}
