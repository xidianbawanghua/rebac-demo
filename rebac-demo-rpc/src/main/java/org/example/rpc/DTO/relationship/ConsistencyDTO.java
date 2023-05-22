package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class ConsistencyDTO {
    private Boolean minimizeLatency;
    private ZedTokenDTO atLeastAsFresh;
    private ZedTokenDTO atExactSnapshot;
    private Boolean fullyConsistent;
}
