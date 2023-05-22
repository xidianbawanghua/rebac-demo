package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.DTO.relationship.*;
import org.example.rpc.RelationshipFeignClient;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RelationshipTest extends BaseTest{
    @Autowired
    private RelationshipFeignClient relationshipFeignClient;
    @Test
    public void testWriteRelationship() {
        WriteRelationshipsRequestDTO requestDTO = generateWriteRequest();
        try {
            WriteRelationshipsResponseDTO responseDTO = relationshipFeignClient.writeRelationships(token, requestDTO);
            System.out.println(responseDTO.getWrittenAt().getToken());
        } catch (FeignException e) {
            log.error("feign msg is {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("error msg is {}", e.getMessage(), e);
            throw e;
        }
    }

    @Test
    public void testReadRelationship() {
        try {
            String response = relationshipFeignClient.readRelationships(token, generateReadRequest());
            String[] splits = response.split("\\{\"result\":");
            List<ReadRelationshipsResponseDTO> results = new ArrayList<>();
            for (String split : splits) {
                if (StringUtils.isBlank(split)) continue;
                ReadRelationshipsResponseDTO responseDTO = objectMapper.readValue("{\"result\":".concat(split), ReadRelationshipsResponseDTO.class);
                results.add(responseDTO);
            }
            System.out.println(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            log.error("error msg is {}", e.getMessage(), e);
        }
    }


    private ReadRelationshipsRequestDTO generateReadRequest() {
        String jsonStr = "{\n" +
                "  \"consistency\": {\n" +
                "    \"fullyConsistent\": true\n" +
                "  },\n" +
                "  \"relationshipFilter\": {\n" +
                "    \"resourceType\": \"role\",\n" +
                "    \"optionalResourceId\": \"lx_role\",\n" +
                "    \"optionalRelation\": \"member\",\n" +
                "    \"optionalSubjectFilter\": {\n" +
                "      \"subjectType\": \"user\",\n" +
                "      \"optionalSubjectId\": null,\n" +
                "      \"optionalRelation\": null\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            return objectMapper.readValue(jsonStr, ReadRelationshipsRequestDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private WriteRelationshipsRequestDTO generateWriteRequest() {
        WriteRelationshipsRequestDTO requestDTO = new WriteRelationshipsRequestDTO();
        List<RelationshipUpdateDTO> updates = new ArrayList<>();
        for (int i=0;i<2;i++) {
            String jsonStr ="{\n" +
                    "      \"operation\": \"OPERATION_CREATE\",\n" +
                    "      \"relationship\": {\n" +
                    "        \"resource\": {\n" +
                    "          \"objectType\": \"role\",\n" +
                    "          \"objectId\": \"lx_role\"\n" +
                    "        },\n" +
                    "        \"relation\": \"member\",\n" +
                    "        \"subject\": {\n" +
                    "          \"object\": {\n" +
                    "            \"objectType\": \"user\",\n" +
                    "            \"objectId\": \"lx\"\n" +
                    "          },\n" +
                    "          \"optionalRelation\": null\n" +
                    "        },\n" +
                    "        \"optionalCaveat\": null\n" +
                    "      }\n" +
                    "    }";
            try {
                RelationshipUpdateDTO relationshipUpdateDTO = objectMapper.readValue(jsonStr, RelationshipUpdateDTO.class);
                relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectId("lx_"+i);
                updates.add(relationshipUpdateDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        requestDTO.setUpdates(updates);
        return requestDTO;
    }

    private WriteRelationshipsRequestDTO generateWriteRequest1() {
        String jsonStr = "{\n" +
                "  \"updates\": [\n" +
                "    {\n" +
                "      \"operation\": \"OPERATION_CREATE\",\n" +
                "      \"relationship\": {\n" +
                "        \"resource\": {\n" +
                "          \"objectType\": \"role\",\n" +
                "          \"objectId\": \"lx_role\"\n" +
                "        },\n" +
                "        \"relation\": \"member\",\n" +
                "        \"subject\": {\n" +
                "          \"object\": {\n" +
                "            \"objectType\": \"user\",\n" +
                "            \"objectId\": \"zhangsan\"\n" +
                "          },\n" +
                "          \"optionalRelation\": null\n" +
                "        },\n" +
                "        \"optionalCaveat\": null\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            WriteRelationshipsRequestDTO requestDTO = objectMapper.readValue(jsonStr, WriteRelationshipsRequestDTO.class);


            RelationshipUpdateDTO newUpdate = objectMapper.readValue(objectMapper.writeValueAsString(requestDTO.getUpdates().get(0)), RelationshipUpdateDTO.class);
            BeanUtils.copyProperties(requestDTO.getUpdates().get(0),newUpdate);
            newUpdate.getRelationship().getSubject().getObject().setObjectId("cccc");
            requestDTO.getUpdates();
            return requestDTO;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
