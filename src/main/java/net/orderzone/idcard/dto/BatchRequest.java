package net.orderzone.idcard.dto;

import net.orderzone.idcard.model.ProfileType;
import java.util.List;

public class BatchRequest {
    private List<Long> selectedIds;
    private ProfileType type;
    private String department;
    private String batchMode; // "SELECTED", "TYPE", "DEPARTMENT"

    // Getters and Setters
    public List<Long> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(List<Long> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBatchMode() {
        return batchMode;
    }

    public void setBatchMode(String batchMode) {
        this.batchMode = batchMode;
    }
}
