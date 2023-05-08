package learn.models;

import java.time.LocalDate;

public class Task {
    private int taskId;
    private String taskName;
    private LocalDate dueDate;
    private boolean status;
    private int checklistId;


    public Task() {
    }
    public Task(int taskId, String taskName, LocalDate dueDate, boolean status, int checklistId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.status = status;
        this.checklistId = checklistId;
    }


    public int getTaskId() { return taskId; }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }

}
