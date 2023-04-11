package com.klinnovations.service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.klinnovations.entity.CitizenPlan;
import com.klinnovations.repo.CitizenPlanRepository;
import com.klinnovations.request.SearchRequest;

@Service
public class ReportServiceImpl<ServletOutputStream> implements ReportService {

	@Autowired
	private CitizenPlanRepository planRepo;

	public List<String> getPlanNames() {

		return planRepo.getPlanNames();

	}

	@Override
	public List<String> getPlanStatuses() {

		return planRepo.getPlanStatus();

	}

	@Override
	public List<CitizenPlan> search(SearchRequest request) {

		CitizenPlan entity = new CitizenPlan();

		if (null != request.getPlanName() && !"".equals(request.getPlanName())) {
			entity.setPlanName(request.getPlanName());
		}

		if (null != request.getPlanStatus() && !"".equals(request.getPlanStatus())) {
			entity.setPlanStatus(request.getPlanStatus());
		}

		if (null != request.getGender() && !"".equals(request.getGender())) {
			entity.setGender(request.getGender());
		}

		if (null != request.getStartDate() && !"".equals(request.getStartDate())) {

			String startDate = request.getStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(startDate, formatter);

			entity.setPlanStartDate(localDate);
		}

		return planRepo.findAll(Example.of(entity));

	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception {
		
		//Workbook workbook = new XSSFWorkbook(); //.xlsx
		Workbook workbook = new HSSFWorkbook(); //.xls
		Sheet sheet = workbook.createSheet("plans-data");
		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Citizen Name");
		headerRow.createCell(2).setCellValue("Plan Name");
		headerRow.createCell(3).setCellValue("Plan Status");
		headerRow.createCell(4).setCellValue("Plan Start Date");
		headerRow.createCell(5).setCellValue("Plan End Date");
		headerRow.createCell(6).setCellValue("Benefit Amount");

		List<CitizenPlan> records = planRepo.findAll();

		int dataRowIndex = 1;
		
		for (CitizenPlan plan : records) {
			Row dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(plan.getCitizenId());
			dataRow.createCell(1).setCellValue(plan.getCitizenName());
			dataRow.createCell(2).setCellValue(plan.getPlanName());
			dataRow.createCell(3).setCellValue(plan.getPlanStatus());
			dataRow.createCell(4).setCellValue(plan.getPlanStartDate() + "");
			dataRow.createCell(5).setCellValue(plan.getPlanEndDate() + "");
			
			if(null != plan.getBenefitAmount()) {
				dataRow.createCell(6).setCellValue(plan.getBenefitAmount());
			}else {
				dataRow.createCell(6).setCellValue("N/A");
			}

			dataRowIndex++;

		}

		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return true;
	}

	@Override
	public boolean exportPdf() {

		return false;
	}

}
