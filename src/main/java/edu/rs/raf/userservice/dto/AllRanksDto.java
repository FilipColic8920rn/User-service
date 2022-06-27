package edu.rs.raf.userservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AllRanksDto {
    private List<RankDto> ranks = new ArrayList<>();

    public AllRanksDto() {
    }

    public AllRanksDto(List<RankDto> ranks) {
        this.ranks = ranks;
    }

    public List<RankDto> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankDto> ranks) {
        this.ranks = ranks;
    }
}
