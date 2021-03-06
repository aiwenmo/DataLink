package com.datalink.flink.result;

import java.time.LocalDate;

/**
 * InsertResult
 *
 * @author qiwenkai
 * @since 2021/5/25 19:08
 **/
public class InsertResult implements IResult {
    private String statement;
    private String jobID;
    private boolean success;
    private long time;
    private LocalDate finishDate;

    public InsertResult(String statement, String jobID, boolean success, long time, LocalDate finishDate) {
        this.statement = statement;
        this.jobID = jobID;
        this.success = success;
        this.time = time;
        this.finishDate = finishDate;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }
}
