package com.example.fastapi.service;

import com.example.fastapi.dboModel.CarProblemReport;
import com.example.fastapi.repository.CarProblemReportRepository;
import com.example.fastapi.repository.CarInfoRepository;
import com.example.fastapi.dboModel.CarInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarProblemReportService {

    @Autowired
    private CarProblemReportRepository reportRepository;

    @Autowired
    private CarInfoRepository carInfoRepository;

    public CarProblemReport createReport(CarProblemReport report) {
        // اگر تاریخ خالی بود، تاریخ فعلی ثبت شود
        if (report.getDateTime() == null) {
            report.setDateTime(new Date());
        }
        return reportRepository.save(report);
    }

    public List<CarProblemReport> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<CarProblemReport> getReportById(String id) {
        return reportRepository.findById(id);
    }

    public List<CarProblemReport> getReportsByCarId(String carId) {
        return reportRepository.findByCarId(carId);
    }

    public List<CarProblemReport> getReportsByCreatorUserId(String userId) {
        return reportRepository.findByCreatorUserId(userId);
    }

    public boolean deleteReport(String id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CarProblemReport> updateReport(String id, CarProblemReport updatedReport) {
        Optional<CarProblemReport> existingOpt = reportRepository.findById(id);
        if (existingOpt.isPresent()) {
            CarProblemReport existing = existingOpt.get();
            existing.setCarId(updatedReport.getCarId());
            existing.setCreatorUserId(updatedReport.getCreatorUserId());
            existing.setProblemSummary(updatedReport.getProblemSummary());
            existing.setDateTime(updatedReport.getDateTime());
            return Optional.of(reportRepository.save(existing));
        }
        return Optional.empty();
    }

    public List<CarProblemReport> getReportsByLicensePlate(String licensePlate) {
        CarInfo carInfo = carInfoRepository.findByLicensePlate(licensePlate);
        if (carInfo != null) {
            return reportRepository.findByCarId(carInfo.getId());
        } else {
            return List.of(); // لیست خالی
        }
    }
}
