package io.memoria.recipes.core.service;

import io.memoria.recipes.core.repo.CategoryRepo;
import reactor.core.publisher.Flux;

@SuppressWarnings("ClassCanBeRecord")
public class CategoryService {
  private final CategoryRepo categoryRepo;

  public CategoryService(CategoryRepo categoryRepo) {
    this.categoryRepo = categoryRepo;
  }

  public Flux<String> categories() {
    return categoryRepo.categories();
  }
}
