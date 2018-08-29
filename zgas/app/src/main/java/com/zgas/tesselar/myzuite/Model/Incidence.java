package com.zgas.tesselar.myzuite.Model;

/**
 * Incidence model
 *
 * @author jarvizu on 28/08/2017
 * @version 2018.0.9
 */
public class Incidence {
    private String userId;
    private String incidenceId;
    private String incidenceDescription;
    private String incidenceTime;

    public Incidence() {
    }

    public Incidence(String userId, String incidenceId, String incidenceDescription, String incidenceTime) {
        this.userId = userId;
        this.incidenceId = incidenceId;
        this.incidenceDescription = incidenceDescription;
        this.incidenceTime = incidenceTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIncidenceId() {
        return incidenceId;
    }

    public void setIncidenceId(String incidenceId) {
        this.incidenceId = incidenceId;
    }

    public String getIncidenceDescription() {
        return incidenceDescription;
    }

    public void setIncidenceDescription(String incidenceDescription) {
        this.incidenceDescription = incidenceDescription;
    }

    public String getIncidenceTime() {
        return incidenceTime;
    }

    public void setIncidenceTime(String incidenceTime) {
        this.incidenceTime = incidenceTime;
    }
}
