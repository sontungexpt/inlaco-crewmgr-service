package com.inlaco.crewmgrservice.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class FacetResult<T> {

  private List<T> dataFacet;

  private List<Map<String, Object>> countFacet;

  public Page<T> toPage(Pageable pageable) {
    return new PageImpl<>(dataFacet, pageable, getCount());
  }

  @JsonCreator
  public FacetResult(List<T> dataFacet, List<Map<String, Object>> countFacet) {
    this.dataFacet = dataFacet;
    this.countFacet = countFacet;
  }

  public static final String getCountKey() {
    return "facetResultCount";
  }

  public static final String getDataFacetName() {
    return "dataFacet";
  }

  public static final String getCountFacetName() {
    return "countFacet";
  }

  public List<T> getDatas() {
    return dataFacet;
  }

  public int getCount(String key) {
    if (countFacet == null || countFacet.isEmpty()) return 0;

    Object count = countFacet.get(0).get(key);
    if (count == null) return 0;
    return (int) count;
  }

  public int getCount() {
    return getCount(getCountKey());
  }
}
