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

import static com.reimed.guid.client.HashCodeItem.combo;
import static com.reimed.guid.client.HashCodeItem.single;
import static com.reimed.guid.client.pii.PIIElement.BIRTHPLACE;
import static com.reimed.guid.client.pii.PIIElement.DAY_OF_BIRTH;
import static com.reimed.guid.client.pii.PIIElement.GENDER;
import static com.reimed.guid.client.pii.PIIElement.GIVEN_NAME;
import static com.reimed.guid.client.pii.PIIElement.MONTH_OF_BIRTH;
import static com.reimed.guid.client.pii.PIIElement.NATIONALITY;
import static com.reimed.guid.client.pii.PIIElement.NATIONAL_ID;
import static com.reimed.guid.client.pii.PIIElement.SURNAME;
import static com.reimed.guid.client.pii.PIIElement.YEAR_OF_BIRTH;
import static net.sf.rubycollect4j.RubyKernel.p;

import com.reimed.guid.client.pii.Nation;
import com.reimed.guid.client.pii.Sex;

public class PIIHashCoderTest {

  public static void main(String... args) {
    HashCodeTemplate template = HashCodeTemplate.builder() //
        .set(HashCodeSet.builder() //
            .item(single(GENDER)) //
            .item(combo(NATIONALITY, NATIONAL_ID)) //
            .item(single(BIRTHPLACE)) //
            .item(single(YEAR_OF_BIRTH)) //
            .build())
        .set(HashCodeSet.builder() //
            .item(single(YEAR_OF_BIRTH)) //
            .item(single(BIRTHPLACE)) //
            .item(combo(SURNAME, GIVEN_NAME)) //
            .item(single(NATIONALITY)) //
            .item(combo(MONTH_OF_BIRTH, DAY_OF_BIRTH)) //
            .build())
        .set(HashCodeSet.builder() //
            .item(single(GENDER)) //
            .item(combo(DAY_OF_BIRTH, MONTH_OF_BIRTH)) //
            .item(single(YEAR_OF_BIRTH)) //
            .item(combo(NATIONALITY, BIRTHPLACE)) //
            .build())
        .build();

    PIIHashCoderFactory
        .setHashCoder(HashCoder.builder().template(template).build());

    p(PIIHashBundle.builder().surname("li").givenName("mj").gender(Sex.MALE)
        .yearOfBirth(1979).monthOfBirth(7).dayOfBirth(21).nationality(Nation.TW)
        .nationalId("A123456789").birthplace(Nation.TW.toString()).build()
        .getHashCodes());
  }

}
