package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.ZedTokenDTO;

@Data
public class ExpandPermissionTreeResponseDTO {
    private ZedTokenDTO expandedAt;
    private PermissionRelationshipTreeDTO treeRoot;
}
