/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package com.reimed.guid.client.pii;

import com.github.wnameless.factorextractor.Factor;

public interface Nationality {

  @Factor(PIIElement.NATIONALITY)
  public Nation getNationality();

}