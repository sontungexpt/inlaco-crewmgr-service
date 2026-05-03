package com.inlaco.crewmgrservice.feature.schedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.schedule.application.port.in.CrewMobilizationScheduleExcelExportUseCase;
import com.inlaco.crewmgrservice.feature.schedule.application.port.out.CrewMobilizationScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrewMobilizationScheduleExcelExportService
    implements CrewMobilizationScheduleExcelExportUseCase {

  private final CrewMobilizationScheduleRepository scheduleRepository;
  private final CrewUseCase crewUseCase;

  @Override
  public byte[] exportSchedule(String scheduleId) {

    CrewMobilizationSchedule schedule =
        scheduleRepository
            .findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

    List<AssignedCrew> crews =
        schedule.getCrews() == null ? List.of() : List.copyOf(schedule.getCrews());

    List<String> cardIds = crews.stream().map(AssignedCrew::getEmployeeCardId).toList();

    Map<String, CrewProfile> profileMap =
        crewUseCase.getProfilesByEmployeeCardIds(cardIds).stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, p -> p, (a, b) -> a));

    try (Workbook workbook = new XSSFWorkbook()) {

      // ======================
      // SHEET 1: INFO
      // ======================
      Sheet infoSheet = workbook.createSheet("Schedule Info");

      createInfoRow(infoSheet, 0, "Schedule ID", schedule.getId());
      createInfoRow(infoSheet, 1, "Partner", schedule.getPartnerName());
      createInfoRow(infoSheet, 2, "Phone", schedule.getPartnerPhone());
      createInfoRow(infoSheet, 3, "Email", schedule.getPartnerEmail());
      createInfoRow(
          infoSheet,
          4,
          "Ship",
          schedule.getShipInfo() != null ? schedule.getShipInfo().getName() : "");
      createInfoRow(infoSheet, 5, "Start Date", String.valueOf(schedule.getStartDate()));
      createInfoRow(infoSheet, 6, "End Date", String.valueOf(schedule.getEndDate()));
      createInfoRow(infoSheet, 7, "Crew Count", String.valueOf(schedule.getCrewNumbers()));

      // ======================
      // SHEET 2: CREW LIST
      // ======================
      Sheet sheet = workbook.createSheet("Crew List");

      String[] headers = {
        "No",
        "Full Name",
        "Employee Card",
        "Rank",
        "Phone",
        "Email",
        "Start Date",
        "End Date",
        "Remark"
      };

      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < headers.length; i++) {
        headerRow.createCell(i).setCellValue(headers[i]);
      }

      int rowIdx = 1;
      int no = 1;

      for (AssignedCrew crew : crews) {

        CrewProfile p = profileMap.get(crew.getEmployeeCardId());

        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(no++);

        row.createCell(1).setCellValue(p != null ? p.getFullName() : "N/A");
        row.createCell(2).setCellValue(crew.getEmployeeCardId());
        row.createCell(3).setCellValue(crew.getRankOnBoard());
        row.createCell(4).setCellValue(p != null ? p.getPhoneNumber() : "");
        row.createCell(5).setCellValue(p != null ? p.getEmail() : "");

        row.createCell(6)
            .setCellValue(crew.getStartDate() != null ? crew.getStartDate().toString() : "");

        row.createCell(7)
            .setCellValue(crew.getEndDate() != null ? crew.getEndDate().toString() : "");

        row.createCell(8).setCellValue(crew.getRemark() != null ? crew.getRemark() : "");
      }

      // tránh autoSize nếu dữ liệu lớn (giữ nhẹ server)
      for (int i = 0; i < headers.length; i++) {
        sheet.setColumnWidth(i, 20 * 256);
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);

      return out.toByteArray();

    } catch (IOException e) {
      throw new RuntimeException("Failed to export Excel", e);
    }
  }

  private void createInfoRow(Sheet sheet, int row, String key, String value) {
    Row r = sheet.createRow(row);
    r.createCell(0).setCellValue(key);
    r.createCell(1).setCellValue(value != null ? value : "");
  }
}
