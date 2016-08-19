package com.xiaoshangxing.xiaoshang.planpropose;

import com.xiaoshangxing.xiaoshang.BaseList;

import java.io.Serializable;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class PlanList extends BaseList implements Serializable{
    private int paticipateNumber;
    private Integer limitPepoleNumber;
    private boolean isFull;
    private String planName;
    private String limitTime;
    private String planProposeTime;
    private boolean isCompleted;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getPlanProposeTime() {
        return planProposeTime;
    }

    public void setPlanProposeTime(String planProposeTime) {
        this.planProposeTime = planProposeTime;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getPaticipateNumber() {
        return paticipateNumber;
    }

    public void setPaticipateNumber(int paticipateNumber) {
        this.paticipateNumber = paticipateNumber;
    }

    public Integer getLimitPepoleNumber() {
        return limitPepoleNumber;
    }

    public void setLimitPepoleNumber(int limitPepoleNumber) {
        this.limitPepoleNumber = limitPepoleNumber;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }


}
