package com.armavi_bsd.robotispnew.model;

public class ZoneModel {
    private String zoneId;
    private String zoneName;
    private String subZoneCount;

    public ZoneModel(String zoneId, String zoneName, String subZoneCount) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.subZoneCount = subZoneCount;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getSubZoneCount() {
        return subZoneCount;
    }

    public void setSubZoneCount(String subZoneCount) {
        this.subZoneCount = subZoneCount;
    }
}
