package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.SubjectReferenceDTO;

import java.util.List;

@Data
public class DirectSubjectSetDTO {
    //todo:repeadted
    private List<SubjectReferenceDTO> subjects;
}
