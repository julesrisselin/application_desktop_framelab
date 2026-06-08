package fr.framelab.Service;

import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.ChallengeDataDTO;

public class ChallengeDemoManager {

    public static ChallengeDTO getDemoChallenge() {
        ChallengeDataDTO data = new ChallengeDataDTO();

        data.setId(1);
        data.setTitle_theme("Mode démo");
        data.setDescription_theme("Ceci est le challenge démo");
        data.setDate_start("08/11/2006");
        data.setDate_end("08/11/2100");
        data.setPicture("challenge/pardefaut.png");

        ChallengeDTO chall = new ChallengeDTO();
        chall.setData(data);

        return chall;
    }
}
