/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package com.reimed.guid.client.pii;

import com.github.wnameless.factorextractor.Factor;

public interface NationalId {

  /**
   * Returns a National ID.
   * 
   * @return a National ID
   */
  @Factor(PIIElement.NATIONAL_ID)
  public String getNationalId();

}
