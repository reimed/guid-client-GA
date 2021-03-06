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

import java.util.Calendar;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.reimed.guid.client.pii.Birthday;

import lombok.NonNull;
import lombok.Value;
import lombok.val;

@Value
public class GreogorianBirthdayConstraint
    implements FactorConstraint<Birthday> {

  @Override
  public Class<Birthday> getTargetType() {
    return Birthday.class;
  }

  @Override
  public FactorConstraintError apply(@NonNull Birthday value) {
    val yearOfBirth = value.getYearOfBirth();
    val monthOfBirth = value.getMonthOfBirth();
    val dayOfBirth = value.getDayOfBirth();

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    if (!range(currentYear - 150, currentYear + 1).contains(yearOfBirth)) {
      return new FactorConstraintError(value.getClass(),
          "Year of birth must be between " + (currentYear - 150) + " and "
              + (currentYear + 1));
    }

    if (!range(1, 12).contains(monthOfBirth)) {
      return new FactorConstraintError(value.getClass(),
          "Month of birth must be between 1 and 12");
    }

    switch (monthOfBirth) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        if (!range(1, 31).contains(value.getDayOfBirth())) {
          return new FactorConstraintError(value.getClass(),
              "Day of birth must be between 1 and 31");
        }
        break;
      case 2:
        if ((yearOfBirth % 4 == 0 && yearOfBirth % 100 != 0)
            || yearOfBirth % 400 == 0) {
          if (!range(1, 29).contains(dayOfBirth)) {
            return new FactorConstraintError(value.getClass(),
                "Day of birth must be between 1 and 29");
          }
        } else {
          if (!range(1, 28).contains(dayOfBirth)) {
            return new FactorConstraintError(value.getClass(),
                "Day of birth must be between 1 and 28");
          }
        }
        break;
      case 4:
      case 6:
      case 9:
      case 11:
        if (!range(1, 30).contains(dayOfBirth)) {
          return new FactorConstraintError(value.getClass(),
              "Day of birth must be between 1 and 30");
        }
        break;
    }

    return null;
  }

  private ContiguousSet<Integer> range(int start, int end) {
    return ContiguousSet.create(Range.closed(start, end),
        DiscreteDomain.integers());
  }

}
