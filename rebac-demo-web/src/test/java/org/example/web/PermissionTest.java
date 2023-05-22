package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.rpc.DTO.permission.ExpandPermissionTreeRequestDTO;
import org.example.rpc.DTO.permission.ExpandPermissionTreeResponseDTO;
import org.example.rpc.DTO.relationship.RelationshipUpdateDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsRequestDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsResponseDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PermissionTest extends BaseTest {

    @Test
    public void testExpand() {
        //写入多层权限数据
        writeMultiLayerRelationships(5);
        //读取relationships树
        ExpandPermissionTreeResponseDTO expandPermissionTreeResponseDTO = permissionFeignClient.expandPermissionTree(token, generateExpandPermissionTreeRequest(namespace + "role", "ancestor_role", "viewer"));
    }


    private ExpandPermissionTreeRequestDTO generateExpandPermissionTreeRequest(String objectType, String objectId, String permission) {
        String jsonStr = "{\n" +
                "  \"consistency\": {\n" +
                "    \"fullyConsistent\": true \n" +
                "  },\n" +
                "  \"resource\": {\n" +
                "    \"objectType\": null,\n" +
                "    \"objectId\": null\n" +
                "  },\n" +
                "  \"permission\": null\n" +
                "}";
        try {
            ExpandPermissionTreeRequestDTO expandPermissionTreeRequestDTO = objectMapper.readValue(jsonStr, ExpandPermissionTreeRequestDTO.class);
            expandPermissionTreeRequestDTO.getResource().setObjectType(objectType);
            expandPermissionTreeRequestDTO.getResource().setObjectId(objectId);
            expandPermissionTreeRequestDTO.setPermission(permission);
            return expandPermissionTreeRequestDTO;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }



    //由于updates的size限制在1000以内，于是进行拆分处理
    public void writeMultiLayerRelationships(int loopCount) {
        int times = loopCount/1000;
        int remainder = loopCount%1000;
        for (int i=0;i<times;i++) {
            WriteRelationshipsRequestDTO requestDTO = generateMultiLayersWriteRelationshipsRequest(1000, i);
            WriteRelationshipsResponseDTO responseDTO = relationshipFeignClient.writeRelationships(token, requestDTO);
            Assert.assertTrue(StringUtils.isNotBlank(responseDTO.getWrittenAt().getToken()));
        }
        if (remainder > 0) {
            WriteRelationshipsRequestDTO requestDTO = generateMultiLayersWriteRelationshipsRequest(remainder, times);
            WriteRelationshipsResponseDTO responseDTO = relationshipFeignClient.writeRelationships(token, requestDTO);
            Assert.assertTrue(StringUtils.isNotBlank(responseDTO.getWrittenAt().getToken()));
        }
    }




    private WriteRelationshipsRequestDTO generateMultiLayersWriteRelationshipsRequest(int loopCount, int random) {
        WriteRelationshipsRequestDTO requestDTO = new WriteRelationshipsRequestDTO();
        List<RelationshipUpdateDTO> updates = new ArrayList<>();
        String ancestorRole = "ancestor_role";
        String parentRole = ancestorRole;

        for (int i=random*loopCount;i<(random+1)*loopCount;i++) {
            String jsonStr ="{\n" +
                    "      \"operation\": \"OPERATION_CREATE\",\n" +
                    "      \"relationship\": {\n" +
                    "        \"resource\": {\n" +
                    "          \"objectType\": \"role\",\n" +
                    "          \"objectId\": \"null\"\n" +
                    "        },\n" +
                    "        \"relation\": \"child\",\n" +
                    "        \"subject\": {\n" +
                    "          \"object\": {\n" +
                    "            \"objectType\": \"role\",\n" +
                    "            \"objectId\": \"null\"\n" +
                    "          },\n" +
                    "          \"optionalRelation\": null\n" +
                    "        },\n" +
                    "        \"optionalCaveat\": null\n" +
                    "      }\n" +
                    "    }";
            try {
                RelationshipUpdateDTO relationshipUpdateDTO = objectMapper.readValue(jsonStr, RelationshipUpdateDTO.class);
                relationshipUpdateDTO.getRelationship().getResource().setObjectType(namespace+"role");
                relationshipUpdateDTO.getRelationship().getResource().setObjectId(parentRole);
                relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectType(namespace+"role");
                relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectId("role_"+i);
                updates.add(relationshipUpdateDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            parentRole = "role_"+i;
        }
        //最后一个role id 为"role_"+(random+1)*loopCount-1,将lx授权至改角色
        String jsonStr ="{\n" +
                "      \"operation\": \"OPERATION_CREATE\",\n" +
                "      \"relationship\": {\n" +
                "        \"resource\": {\n" +
                "          \"objectType\": \"role\",\n" +
                "          \"objectId\": \"null\"\n" +
                "        },\n" +
                "        \"relation\": \"member\",\n" +
                "        \"subject\": {\n" +
                "          \"object\": {\n" +
                "            \"objectType\": \"user\",\n" +
                "            \"objectId\": \"null\"\n" +
                "          },\n" +
                "          \"optionalRelation\": null\n" +
                "        },\n" +
                "        \"optionalCaveat\": null\n" +
                "      }\n" +
                "    }";
        try {
            RelationshipUpdateDTO relationshipUpdateDTO = objectMapper.readValue(jsonStr, RelationshipUpdateDTO.class);
            relationshipUpdateDTO.getRelationship().getResource().setObjectType(namespace+"role");
            relationshipUpdateDTO.getRelationship().getResource().setObjectId("role_"+((random+1)*loopCount-1));
            relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectType(namespace+"user");
            relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectId("lx");
            updates.add(relationshipUpdateDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        requestDTO.setUpdates(updates);
        return requestDTO;
    }


}
