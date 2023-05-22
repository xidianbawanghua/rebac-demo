package org.example.rpc.DTO.permission;

import lombok.Data;

import java.util.List;

@Data
public class PartialCaveatInfoDTO {
    private List<String> missingRequiredContext;
}
