package com.example.fastapi.dto;

import java.util.List;

public class FilterRequestDTO{

    private List<String> taskStatusNames;
    private String startDate;  // تاریخ به صورت رشته (مثلاً "yyyy-MM-dd")
    private String endDate;
    private String licensePlate;

    // Constructors
    public FilterRequestDTO() {
    }

    public FilterRequestDTO(List<String> taskStatusNames, String startDate, String endDate, String licensePlate) {
        this.taskStatusNames = taskStatusNames;
        this.startDate = startDate;
        this.endDate = endDate;
        this.licensePlate = licensePlate;
    }

    // Getters and Setters
    public List<String> getTaskStatusNames() {
        return taskStatusNames;
    }

    public void setTaskStatusNames(List<String> taskStatusNames) {
        this.taskStatusNames = taskStatusNames;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
