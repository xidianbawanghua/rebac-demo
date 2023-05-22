package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class ContextualizedCaveatDTO {
    private String caveatName;
    private StructDTO context;
}
