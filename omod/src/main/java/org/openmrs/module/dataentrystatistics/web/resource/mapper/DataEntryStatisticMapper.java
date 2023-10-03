package org.openmrs.module.dataentrystatistics.web.resource.mapper;

public class DataEntryStatisticMapper {

    private String  userFullName;

    private String entryType;

    private Integer numberOfEntries;

    private Integer numberOfObs;

    private String groupBy ;


    public DataEntryStatisticMapper(String userFullName, String entryType, Integer numberOfEntries, Integer numberOfObs, String groupBy) {
        this.userFullName = userFullName;
        this.entryType = entryType;
        this.numberOfEntries = numberOfEntries;
        this.numberOfObs = numberOfObs;
        this.groupBy = groupBy;
    }

    public DataEntryStatisticMapper() {
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Integer getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(Integer numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public Integer getNumberOfObs() {
        return numberOfObs;
    }

    public void setNumberOfObs(Integer numberOfObs) {
        this.numberOfObs = numberOfObs;
    }

    public Object getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}
