package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.ConsistencyDTO;
import org.example.rpc.DTO.relationship.ObjectReferenceDTO;

@Data
public class ExpandPermissionTreeRequestDTO {
    private ConsistencyDTO consistency;
    private ObjectReferenceDTO resource;
    private String permission;
}
