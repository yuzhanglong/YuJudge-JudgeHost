package com.yzl.judgehost.dto;


import javax.validation.constraints.NotNull;

/**
 * 设置线程池工作数目的数据传输对象
 *
 * @author yuzhanglong
 * @date 2020-8-29 17:16:44
 */
public class SetWorkingAmountDTO {
    @NotNull
    private Integer maxWorkingAmount;

    private Boolean forceSet;

    public Integer getMaxWorkingAmount() {
        return maxWorkingAmount;
    }

    public void setMaxWorkingAmount(Integer maxWorkingAmount) {
        this.maxWorkingAmount = maxWorkingAmount;
    }

    public Boolean getForceSet() {
        return forceSet;
    }

    public void setForceSet(Boolean forceSet) {
        this.forceSet = forceSet;
    }

    @Override
    public String toString() {
        return "SetWorkingAmountDTO{" +
                "maxWorkingAmount=" + maxWorkingAmount +
                '}';
    }
}
