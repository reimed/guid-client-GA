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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Value
public class FactorValidator {

  @SuppressWarnings("rawtypes")
  @Singular
  @NonNull
  List<FactorConstraint> constraints;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public List<FactorConstraintError> validate(Object... objects) {
    List<FactorConstraintError> errors = newArrayList();

    for (Object o : objects) {
      for (FactorConstraint fc : constraints) {
        if (fc.getTargetType().isAssignableFrom(o.getClass())) {
          val error = fc.apply(o);
          if (error != null) errors.add(error);
        }
      }
    }

    log.debug(errors.toString());
    return errors;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public boolean softValidate(Object... objects) {
    for (Object o : objects) {
      for (FactorConstraint fc : constraints) {
        if (fc.getTargetType().isAssignableFrom(o.getClass())) {
          if (fc.apply(o) != null) return false;
        }
      }
    }

    return true;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void hardValidate(Object... objects) {
    for (Object o : objects) {
      for (FactorConstraint fc : constraints) {
        if (fc.getTargetType().isAssignableFrom(o.getClass())) {
          val error = fc.apply(o);
          if (error != null) {
            throw new IllegalStateException(
                error.getType() + ": " + error.getMessage());
          }
        }
      }
    }
  }

}
