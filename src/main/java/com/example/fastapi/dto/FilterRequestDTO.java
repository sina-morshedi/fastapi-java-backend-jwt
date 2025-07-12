package com.example.fastapi.dto;

import java.time.Instant;
import java.util.List;

public class FilterRequestDTO {

    private List<String> taskStatusNames;
    private Instant startDate;       // ← تاریخ با زمان کامل به‌صورت UTC
    private Instant endDate;         // ← همین‌طور
    private String licensePlate;

    public FilterRequestDTO() {
    }

    public FilterRequestDTO(List<String> taskStatusNames, Instant startDate, Instant endDate, String licensePlate) {
        this.taskStatusNames = taskStatusNames;
        this.startDate = startDate;
        this.endDate = endDate;
        this.licensePlate = licensePlate;
    }

    public List<String> getTaskStatusNames() {
        return taskStatusNames;
    }

    public void setTaskStatusNames(List<String> taskStatusNames) {
        this.taskStatusNames = taskStatusNames;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
