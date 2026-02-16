package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.Description;

public record CreateCategoryCommand(String name, String description) {
}
