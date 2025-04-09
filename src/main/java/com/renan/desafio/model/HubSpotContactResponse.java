package com.renan.desafio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubSpotContactResponse {
    private String id;
    private Properties properties;
    private String createdAt;
    private String updatedAt;
    private boolean archived;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        private String company;
        private String createdate;
        private String email;
        private String firstname;
        private String hs_all_contact_vids;
        private String hs_associated_target_accounts;
        private String hs_currently_enrolled_in_prospecting_agent;
        private String hs_email_domain;
        private String hs_full_name_or_email;
        private String hs_is_contact;
        private String hs_is_unworked;
        private String hs_lifecyclestage_lead_date;
        private String hs_marketable_status;
        private String hs_marketable_until_renewal;
        private String hs_membership_has_accessed_private_content;
        private String hs_object_id;
        private String hs_object_source;
        private String hs_object_source_id;
        private String hs_object_source_label;
        private String hs_pipeline;
        private String hs_prospecting_agent_actively_enrolled_count;
        private String hs_registered_member;
        private String hs_searchable_calculated_phone_number;
        private String hs_sequences_actively_enrolled_count;
        private String lastmodifieddate;
        private String lastname;
        private String lifecyclestage;
        private String num_notes;
        private String phone;
    }
}
