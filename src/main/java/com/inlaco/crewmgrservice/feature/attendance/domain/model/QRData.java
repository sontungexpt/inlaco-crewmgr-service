package com.inlaco.crewmgrservice.feature.attendance.domain.model;

// Represents the data extracted from a QR token (could be JWT or other signed format)
public record QRData(String personId, String scheduleId, String companyId) {
  // No further logic needed for a simple record
}
