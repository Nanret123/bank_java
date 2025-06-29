package com.example.bank.common.util;

import java.util.Random;

public class BvnGenerator {
  private static final Random random = new Random();

  public static String generateBvn() {
    StringBuilder bvn = new StringBuilder(11);
    for (int i = 0; i < 11; i++) {
      bvn.append(random.nextInt(10)); // digits 0-9
    }
    return bvn.toString();
  }
}
