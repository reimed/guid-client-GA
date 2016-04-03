/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package com.reimed.guid.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class HashCodeItem implements Iterable<String> {

  @NonNull
  @Singular
  List<String> fields;

  public static HashCodeItem single(String field) {
    return HashCodeItem.builder().field(field).build();
  }

  @SafeVarargs
  public static HashCodeItem combo(String field1, String field2,
      String... fields) {
    return HashCodeItem.builder().field(field1).field(field2)
        .fields(Arrays.asList(fields)).build();
  }

  @Override
  public Iterator<String> iterator() {
    return fields.iterator();
  }

}
