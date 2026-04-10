package com.dmoney.dmoney.tracker.domain.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;

import java.util.Date;
import java.util.List;

public class Movement {
    UserId userId;
    CategoryId catId;
    SubcategoryId subcatId;
    Amount amount;
    MovementType type;
    MovementDescription description;
    Date date;
    List<Tag> tags;




}
