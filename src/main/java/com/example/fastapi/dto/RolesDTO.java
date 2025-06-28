package com.example.fastapi.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RolesDTO {

    @JsonProperty("_id")
    private String id;
    private String roleName;

    public RolesDTO() {}

    public RolesDTO(String id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RolesDTO{" +
                "id='" + id + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}
