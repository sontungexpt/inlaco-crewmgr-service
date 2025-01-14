package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidates")
@JsonIgnoreProperties(
    value = {"recruimentPostId", "id", "status", "accountId"},
    allowGetters = true)
@CompoundIndexes({
  @CompoundIndex(
      name = "profile_search_index",
      def = "{'fullName': 1, 'email': 1, 'phoneNumber': 1}")
})
public class CandidateProfile extends BasicProfile {

  @Schema(description = "The language skills of the sailor", type = "List<String>")
  protected List<String> languageSkills;

  @Schema(description = "The experience of the sailor", type = "List<String>")
  protected List<String> experiences;

  @Schema(
      description = "Recruitment post id",
      example = "60f3b3b3b3b3b3b3b3b3b3b3",
      requiredMode = Schema.RequiredMode.REQUIRED,
      hidden = true,
      type = "string")
  private ObjectId recruimentPostId;

  @JsonGetter("recruimentPostId")
  public String getRecruimentPostIdString() {
    return recruimentPostId.toString();
  }

  @JsonIgnore
  public ObjectId getRecruimentPostId() {
    return recruimentPostId;
  }

  @Schema(description = "Resume file", example = "file")
  private File resume;

  @Schema(description = "Interview score", example = "0")
  private int interviewScore;

  @Schema(description = "Interview feedback", example = "Good")
  public enum Status {
    @Schema(description = "Applied for the job", example = "APPLIED")
    APPLIED,

    @Schema(description = "Waiting for interview", example = "WAIT_FOR_INTERVIEW")
    WAIT_FOR_INTERVIEW,

    @Schema(description = "Interviewed but was rejected", example = "REJECTED")
    REJECTED,

    @Schema(description = "Hired", example = "HIRED")
    HIRED
  }

  @Default
  @Schema(
      description = "Candidate status",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED,
      example = "APPLIED")
  @JsonPatchIgnore
  private Status status = Status.APPLIED;

  @CreatedDate
  @Schema(hidden = true)
  @JsonPatchIgnore
  @JsonIgnore
  private Instant appliedAt;

  public Instant getAppliedDate() {
    return appliedAt;
  }

  @LastModifiedDate
  @Schema(hidden = true)
  @JsonPatchIgnore
  @JsonIgnore
  private Instant updatedAt;

  public Instant getUpdatedDate() {
    return updatedAt;
  }

  @Schema(hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  @CreatedBy
  private ObjectId createdBy;

  @LastModifiedBy
  @JsonIgnore
  @JsonPatchIgnore
  @Schema(hidden = true)
  private ObjectId updatedBy;
}
