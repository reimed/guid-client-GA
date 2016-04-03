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

import java.util.Iterator;
import java.util.List;

import com.reimed.guid.client.pii.Nation;
import com.reimed.guid.client.pii.PII;
import com.reimed.guid.client.pii.Sex;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class PIIHashBundle implements PII, HashBundle, Iterable<String> {

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
  Nation nationality;

  @NonNull
  String nationalId;

  @NonNull
  String birthplace;

  PIIHashBundle(String surname, String givenName, Sex gender, int yearOfBirth,
      int monthOfBirth, int dayOfBirth, Nation nationality, String nationalId,
      String birthplace) {
    this.surname = surname.toUpperCase().trim();
    this.givenName = givenName.toUpperCase().trim();
    this.gender = gender;
    this.yearOfBirth = yearOfBirth;
    this.monthOfBirth = monthOfBirth;
    this.dayOfBirth = dayOfBirth;
    this.nationality = nationality;
    this.nationalId = nationalId.toUpperCase().trim();
    this.birthplace = birthplace.toUpperCase().trim();
    PIIValidationFactory.validate(this);
  }

  @Override
  public List<String> getHashCodes() {
    return PIIHashCoderFactory.getHashCoder().getHashCodes(this);
  }

  @Override
  public Iterator<String> iterator() {
    return getHashCodes().iterator();
  }

}
