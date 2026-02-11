package fr.framelab.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeDTO {
    private ChallengeDataDTO data;

    public ChallengeDTO(){}

    public ChallengeDataDTO getData() {
        return data;
    }

    public void setData(ChallengeDataDTO data) {
        this.data = data;
    }
}

