package com.inlaco.crewmgrservice.feature.course.domain.enums;

public enum CourseMemberStatus {
  PENDING, // The course is pending to start
  IN_PROGRESS, // The course is currently in progress
  COMPLETED, // The course has been successfully completed
  EXPIRED, // The course has expired
  CANCELLED, // The course was cancelled
  UNKNOWN // The status is unknown
}
