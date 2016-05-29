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
package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.common.collect.ImmutableSet;

@EqualsAndHashCode(callSuper = false)
@ToString
public class HttpOptional<T> extends Optional<T> {

  private static final long serialVersionUID = 1L;

  @Getter
  @Setter
  private int httpStatus = 200;

  @Getter
  @Setter
  private List<String> httpMessages = newArrayList();

  private final T reference;

  public HttpOptional(T reference) {
    this.reference = reference;
  }

  @Override
  public boolean isPresent() {
    return reference != null;
  }

  @Override
  public T get() {
    checkState(isPresent());
    return reference;
  }

  @Override
  public T or(T defaultValue) {
    return isPresent() ? get() : defaultValue;
  }

  @Override
  public Optional<T> or(Optional<? extends T> secondChoice) {
    return isPresent() ? this : Optional.<T> of(secondChoice.get());
  }

  @Override
  public T or(Supplier<? extends T> supplier) {
    return isPresent() ? get() : checkNotNull(supplier.get());
  }

  @Override
  public T orNull() {
    return isPresent() ? get() : null;
  }

  @Override
  public Set<T> asSet() {
    return isPresent() ? ImmutableSet.<T> of(get()) : ImmutableSet.<T> of();
  }

  @Override
  public <V> Optional<V> transform(Function<? super T, V> function) {
    return isPresent() ? Optional.of(checkNotNull(function.apply(reference)))
        : Optional.<V> absent();
  }

}
