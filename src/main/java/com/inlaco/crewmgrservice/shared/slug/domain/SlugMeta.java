package com.inlaco.crewmgrservice.shared.slug.domain;

import java.lang.reflect.Field;
import java.util.List;

public record SlugMeta(
    Field slugField, List<Field> sourceFields, boolean hyphen, boolean lowerCase) {}
