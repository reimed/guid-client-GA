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

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.hash.Hashing.sha512;

import java.util.List;
import java.util.Map;

import com.github.wnameless.factorextractor.FactorExtractor;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Value
public class HashCoder {

  @NonNull
  HashFunction hashingFunction;
  @NonNull
  HashCodeTemplate template;
  @NonNull
  String delimiter;
  @NonNull
  HashCodeSetFuction setFunction;

  HashCoder(HashFunction hashFunction, HashCodeTemplate template,
      final String delimiter, HashCodeSetFuction setFunction) {
    this.hashingFunction = hashFunction != null ? hashFunction : sha512();
    this.template = template;
    this.delimiter = delimiter != null ? delimiter : "_";
    val deli = this.delimiter;
    this.setFunction =
        setFunction != null ? setFunction : new HashCodeSetFuction() {

          @Override
          public String apply(HashCodeSet set, Map<String, Object> factors) {
            StringBuilder sb = new StringBuilder(deli);
            for (HashCodeItem item : set) {
              for (String field : item) {
                Object factor = checkNotNull(factors.get(field));
                sb.append(factor);
              }
              sb.append(deli);
            }

            return sb.toString();
          }

        };
  }

  public List<String> hashObjects(Object... objects) {
    Map<String, Object> factors = FactorExtractor.extract(objects);

    List<String> hashCodes = newArrayList();
    for (HashCodeSet set : template) {
      String setStr = setFunction.apply(set, factors);
      log.debug(setStr);
      HashCode hashCode = hashingFunction.hashBytes(setStr.getBytes(UTF_8));
      hashCodes.add(hashCode.toString());
    }

    return hashCodes;
  }

  public interface HashCodeSetFuction {

    public String apply(HashCodeSet set, Map<String, Object> factors);

  }

}
