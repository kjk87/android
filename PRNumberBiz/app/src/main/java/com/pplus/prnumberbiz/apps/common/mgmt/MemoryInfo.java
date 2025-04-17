package com.pplus.prnumberbiz.apps.common.mgmt;

/**
 * Created by ksh on 2017-01-19.
 */

public class MemoryInfo {
    private long availMem;
    private long totalMem;
    private double availableMegs;
    private int availablePercent;

    public long getAvailMem(){

        return availMem;
    }

    public void setAvailMem(long availMem){

        this.availMem = availMem;
    }

    public long getTotalMem(){

        return totalMem;
    }

    public void setTotalMem(long totalMem){

        this.totalMem = totalMem;
    }

    public double getAvailableMegs(){

        return availableMegs;
    }

    public void setAvailableMegs(double availableMegs){

        this.availableMegs = availableMegs;
    }

    public int getAvailablePercent(){

        return availablePercent;
    }

    public void setAvailablePercent(int availablePercent){

        this.availablePercent = availablePercent;
    }

    @Override
    public String toString(){

        return "MemoryInfo{" +
                "availMem=" + availMem +
                ", totalMem=" + totalMem +
                ", availableMegs=" + availableMegs +
                ", availablePercent=" + availablePercent +
                '}';
    }
}
