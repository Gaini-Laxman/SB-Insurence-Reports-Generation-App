package com.klinnovations.service;

import java.util.List;

import com.klinnovations.entity.CitizenPlan;
import com.klinnovations.request.SearchRequest;

public interface ReportService {

	// abstract Method
	public List<String> getPlanNames();

	public List<String> getPlanStatuses();

	public List<CitizenPlan> search(SearchRequest request);

	public boolean exportExcel();

	public boolean exportPdf();

}
