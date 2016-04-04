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
package com.reimed.guid.client.pii;

import com.neovisionaries.i18n.CountryCode;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class BasePII implements PII {

  @NonNull
  String surname;

  @NonNull
  String givenName;

  @NonNull
  Sex gender;

  int yearOfBirth;

  int monthOfBirth;

  int dayOfBirth;

  @NonNull
  CountryCode nationality;

  @NonNull
  String nationalId;

  @NonNull
  String birthplace;

  BasePII(String surname, String givenName, Sex gender, int yearOfBirth,
      int monthOfBirth, int dayOfBirth, CountryCode nationality,
      String nationalId, String birthplace) {
    this.surname = surname.toUpperCase().trim();
    this.givenName = givenName.toUpperCase().trim();
    this.gender = gender;
    this.yearOfBirth = yearOfBirth;
    this.monthOfBirth = monthOfBirth;
    this.dayOfBirth = dayOfBirth;
    this.nationality = nationality;
    this.nationalId = nationalId.toUpperCase().trim();
    this.birthplace = birthplace.toUpperCase().trim();
  }

}
