package com.pedro.aviationapi.infrastructure.clients.AviationApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AviationApiAirportResponse {

    @JsonProperty("site_number")
    public String siteNumber;

    @JsonProperty("type")
    public String type;

    @JsonProperty("facility_name")
    public String facilityName;

    @JsonProperty("faa_ident")
    public String faaIdent;

    @JsonProperty("icao_ident")
    public String icaoIdent;

    @JsonProperty("district_office")
    public String districtOffice;

    public String state;

    @JsonProperty("state_full")
    public String stateFull;

    public String county;
    public String city;
    public String ownership;
    public String use;
    public String manager;

    @JsonProperty("manager_phone")
    public String managerPhone;

    public String latitude;

    @JsonProperty("latitude_sec")
    public String latitudeSec;

    public String longitude;

    @JsonProperty("longitude_sec")
    public String longitudeSec;

    public String elevation;

    @JsonProperty("magnetic_variation")
    public String magneticVariation;

    public String tpa;

    @JsonProperty("vfr_sectional")
    public String vfrSectional;

    @JsonProperty("boundary_artcc")
    public String boundaryArtcc;

    @JsonProperty("boundary_artcc_name")
    public String boundaryArtccName;

    @JsonProperty("responsible_artcc")
    public String responsibleArtcc;

    @JsonProperty("responsible_artcc_name")
    public String responsibleArtccName;

    @JsonProperty("fss_phone_number")
    public String fssPhoneNumber;

    @JsonProperty("fss_phone_numer_tollfree")
    public String fssPhoneNumberTollfree;

    @JsonProperty("notam_facility_ident")
    public String notamFacilityIdent;

    public String status;

    @JsonProperty("certification_typedate")
    public String certificationTypedate;

    @JsonProperty("customs_airport_of_entry")
    public String customsAirportOfEntry;

    @JsonProperty("military_joint_use")
    public String militaryJointUse;

    @JsonProperty("military_landing")
    public String militaryLanding;

    @JsonProperty("lighting_schedule")
    public String lightingSchedule;

    @JsonProperty("beacon_schedule")
    public String beaconSchedule;

    @JsonProperty("control_tower")
    public String controlTower;

    public String unicom;
    public String ctaf;

    @JsonProperty("effective_date")
    public String effectiveDate;
}
