package com.inlaco.crewmgrservice.common.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
public abstract class FacetResult<T> {

  public static final String COUNT_KEY = "facetResultCount";
  public static final String DATA_FACET_NAME = "dataFacet";
  public static final String COUNT_FACET_NAME = "countFacet";

  private List<T> dataFacet;
  private List<Map<String, Object>> countFacet;

  protected FacetResult() {}

  protected FacetResult(List<T> dataFacet, List<Map<String, Object>> countFacet) {
    this.dataFacet = dataFacet;
    this.countFacet = countFacet;
  }

  public static final String getCountKey() {
    return COUNT_KEY;
  }

  public static final String getDataFacetName() {
    return DATA_FACET_NAME;
  }

  public static final String getCountFacetName() {
    return COUNT_FACET_NAME;
  }

  public Page<T> toPage(Pageable pageable) {
    return toPage(pageable, COUNT_KEY);
  }

  public Page<T> toPage(Pageable pageable, String countKey) {
    return new PageImpl<>(dataFacet, pageable, getCount(countKey));
  }

  public List<T> data() {
    return dataFacet;
  }

  public long getCount() {
    return getCount(COUNT_KEY);
  }

  public long getCount(String key) {
    if (countFacet == null || countFacet.isEmpty()) return 0;
    Object value = countFacet.get(0).get(key);
    if (!(value instanceof Number number)) return 0;
    return number.longValue();
  }
}
