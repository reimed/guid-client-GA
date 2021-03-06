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

import lombok.Value;

@Value
public class FactorConstraints {

  public static final GreogorianBirthdayConstraint greogorianBirthday =
      new GreogorianBirthdayConstraint();

  public static final UpperCaseBirthplaceConstraint upperCaseBirthplace =
      new UpperCaseBirthplaceConstraint();

  public static final UpperCaseNameConstraint upperCaseName =
      new UpperCaseNameConstraint();

  public static final UpperCaseNationalIdConstraint upperCaseNationalId =
      new UpperCaseNationalIdConstraint();

}
