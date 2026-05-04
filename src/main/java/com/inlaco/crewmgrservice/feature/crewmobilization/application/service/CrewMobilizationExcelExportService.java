package com.inlaco.crewmgrservice.feature.crewmobilization.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationExcelExportUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrewMobilizationExcelExportService implements CrewMobilizationExcelExportUseCase {

  private final CrewMobilizationRepository scheduleRepository;
  private final CrewUseCase crewUseCase;

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  @Override
  public byte[] exportSchedule(String scheduleId) {

    CrewMobilizationSchedule s =
        scheduleRepository
            .findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

    List<AssignedCrew> crews =
        Optional.ofNullable(s.getCrews()).map(ArrayList::new).orElseGet(ArrayList::new);

    Map<String, CrewProfile> profileMap =
        crewUseCase
            .getProfilesByEmployeeCardIds(
                crews.stream().map(AssignedCrew::getEmployeeCardId).toList())
            .stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, p -> p, (a, b) -> a));

    try (Workbook wb = new XSSFWorkbook()) {

      buildInfoSheet(wb, s);
      buildCrewSummary(wb, crews, profileMap);
      buildCrewDetail(wb, crews, profileMap);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      wb.write(out);
      return out.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Export Excel failed", e);
    }
  }

  // ========================= INFO SHEET =========================
  private void buildInfoSheet(Workbook wb, CrewMobilizationSchedule s) {
    Sheet sh = wb.createSheet("Mobilization Info");
    int r = 0;

    row(sh, r++, "Mobilization ID", s.getId());
    row(sh, r++, "Partner Name", s.getPartnerName());
    row(sh, r++, "Partner Phone", s.getPartnerPhone());
    row(sh, r++, "Partner Email", s.getPartnerEmail());
    row(sh, r++, "Partner Address", s.getPartnerAddress());

    if (s.getShipInfo() != null) {
      row(sh, r++, "Ship Name", s.getShipInfo().getName());
      row(sh, r++, "Ship IMO", s.getShipInfo().getImoNumber());
      row(sh, r++, "Ship Flag", s.getShipInfo().getCountryISO());
      row(sh, r++, "Ship Type", s.getShipInfo().getType());
      row(sh, r++, "Ship Description", s.getShipInfo().getDescription());
    }

    row(sh, r++, "Start", fmt(s.getStartDate()));
    row(sh, r++, "End", fmt(s.getEndDate()));
    row(sh, r++, "Status", String.valueOf(s.getStatus()));
    row(sh, r, "Crew Count", String.valueOf(s.getCrewNumbers()));
  }

  // ========================= CREW SUMMARY =========================
  private void buildCrewSummary(
      Workbook wb, List<AssignedCrew> crews, Map<String, CrewProfile> map) {

    Sheet sh = wb.createSheet("Crew Summary");

    String[] h = {"No", "Name", "Card", "Rank", "Phone", "Email"};
    header(sh, h);

    int i = 1;
    for (AssignedCrew c : crews) {
      CrewProfile p = map.get(c.getEmployeeCardId());
      Row r = sh.createRow(i++);

      r.createCell(0).setCellValue(i - 1);
      r.createCell(1).setCellValue(get(p != null ? p.getFullName() : "N/A"));
      r.createCell(2).setCellValue(c.getEmployeeCardId());
      r.createCell(3).setCellValue(get(c.getRankOnBoard()));
      r.createCell(4).setCellValue(get(p != null ? p.getPhoneNumber() : ""));
      r.createCell(5).setCellValue(get(p != null ? p.getEmail() : ""));
    }
  }

  // ========================= CREW DETAIL =========================
  private void buildCrewDetail(
      Workbook wb, List<AssignedCrew> crews, Map<String, CrewProfile> map) {

    Sheet sh = wb.createSheet("Crew Detail");

    String[] h = {
      "No",
      "Name",
      "Card",
      "Rank",
      "Gender",
      "Phone",
      "Email",
      "Address",
      "Birth",
      "Citizen",
      "Social",
      "Accident",
      "Start",
      "End",
      "Remark"
    };
    header(sh, h);

    int i = 1;
    for (AssignedCrew c : crews) {
      CrewProfile p = map.get(c.getEmployeeCardId());
      Row r = sh.createRow(i++);

      r.createCell(0).setCellValue(i - 1);
      r.createCell(1).setCellValue(get(p != null ? p.getFullName() : ""));
      r.createCell(2).setCellValue(c.getEmployeeCardId());
      r.createCell(3).setCellValue(get(c.getRankOnBoard()));

      r.createCell(4).setCellValue(p == null ? "" : String.valueOf(p.getGender()));

      r.createCell(5).setCellValue(get(p != null ? p.getPhoneNumber() : ""));
      r.createCell(6).setCellValue(get(p != null ? p.getEmail() : ""));
      r.createCell(7).setCellValue(get(p != null ? p.getAddress() : ""));

      r.createCell(8).setCellValue(fmt(p != null ? p.getBirthDate() : null));
      r.createCell(9).setCellValue(get(p != null ? p.getCitizenIdentityCardId() : ""));
      r.createCell(10).setCellValue(get(p != null ? p.getSocialInsuranceCode() : ""));
      r.createCell(11).setCellValue(get(p != null ? p.getAccidentInsuranceCode() : ""));

      r.createCell(12).setCellValue(fmt(c.getStartDate()));
      r.createCell(13).setCellValue(fmt(c.getEndDate()));
      r.createCell(14).setCellValue(get(c.getRemark()));
    }

    for (int c = 0; c < h.length; c++) sh.setColumnWidth(c, 22 * 256);
  }

  // ========================= HELPERS =========================
  private void header(Sheet sh, String[] h) {
    Row r = sh.createRow(0);
    for (int i = 0; i < h.length; i++) r.createCell(i).setCellValue(h[i]);
  }

  private void row(Sheet sh, int i, String k, String v) {
    Row r = sh.createRow(i);
    r.createCell(0).setCellValue(k);
    r.createCell(1).setCellValue(get(v));
  }

  private String get(String v) {
    return v == null ? "" : v;
  }

  private String fmt(Object o) {
    if (o == null) return "";
    if (o instanceof Instant) return ((Instant) o).atZone(ZoneId.systemDefault()).format(FMT);
    return o.toString();
  }
}
