package io.memoria.recipes.core;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.jutils.jcore.id.IdGenerator;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class Tests {

  public static final IdGenerator idGen = () -> Id.of(0);
  public static final Supplier<LocalDateTime> t = () -> LocalDateTime.of(2000, 1, 1, 0, 0);

  private Tests() {}
}
