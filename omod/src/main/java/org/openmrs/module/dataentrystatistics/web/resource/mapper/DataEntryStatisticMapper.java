package org.openmrs.module.dataentrystatistics.web.resource.mapper;

public class DataEntryStatisticMapper {

    private String personUuid;

    private String fullName;

    private String entryType;

    private Integer numberOfEntries;

    private Integer numberOfObs;

    private String groupBy ;


    public DataEntryStatisticMapper(String fullName, String personUuid, String entryType, Integer numberOfEntries, Integer numberOfObs, String groupBy) {
        this.fullName = fullName;
        this.personUuid = personUuid;
        this.entryType = entryType;
        this.numberOfEntries = numberOfEntries;
        this.numberOfObs = numberOfObs;
        this.groupBy = groupBy;
    }

    public DataEntryStatisticMapper() {
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
