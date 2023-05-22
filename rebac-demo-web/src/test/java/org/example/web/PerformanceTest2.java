package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.DTO.permission.CheckPermissionRequestDTO;
import org.example.rpc.DTO.permission.CheckPermissionResponseDTO;
import org.example.rpc.DTO.relationship.RelationshipUpdateDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsRequestDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsResponseDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

//测试多层
@Slf4j
public class PerformanceTest2 extends BaseTest{

    //写入10层关系数据
    @Test
    public void test10() {
        //写入10层关系数据
        writeMultiLayerRelationships(10);

        //调用鉴权接口，查看鉴权性能(lx,ancestor_role)
        checkPermissions();
    }

    @Test
    public void test20() {
        //写入10层关系数据
        writeMultiLayerRelationships(20);

        //调用鉴权接口，查看鉴权性能(lx,ancestor_role)
        checkPermissions();
    }

    @Test
    public void test30() {
        //写入10层关系数据
        writeMultiLayerRelationships(30);

        //调用鉴权接口，查看鉴权性能(lx,ancestor_role)
        checkPermissions();
    }

    @Test
    public void test40() {
        //写入10层关系数据
        writeMultiLayerRelationships(40);

        //调用鉴权接口，查看鉴权性能(lx,ancestor_role)
        checkPermissions();
    }


    //注意⚠️报错： [{"code":2,"message":"max depth exceeded: this usually indicates a recursive or too deep data dependency","details":[]}]
    //见spicedb accessibilityset.go最大限制maxDepth为50
    @Test
    public void test50() {
        //写入10层关系数据
        writeMultiLayerRelationships(49);

        //调用鉴权接口，查看鉴权性能(lx,ancestor_role)
        checkPermissions();
    }








    private void checkPermissions() {
        log.info("调用鉴权接口");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CheckPermissionResponseDTO checkPermissionResponseDTO = permissionFeignClient.checkPermission(token, generateCheckPermissionRequest(namespace+"role", "ancestor_role", namespace+"user", "lx"));
        stopWatch.stop();
        Assert.assertTrue(checkPermissionResponseDTO.getPermissionship().name().equals("PERMISSIONSHIP_HAS_PERMISSION"));
        log.info("cost time(ms) : {}", stopWatch.getTotalTimeMillis());
    }





    private CheckPermissionRequestDTO generateCheckPermissionRequest(String objectType, String objectId, String subjectType, String subjectId) {
        String jsonStr = "{\n" +
                "  \"consistency\": {\n" +
                "    \"fullyConsistent\": true\n" +
                "  },\n" +
                "  \"resource\": {\n" +
                "    \"objectType\": \"role\",\n" +
                "    \"objectId\": \"null\"\n" +
                "  },\n" +
                "  \"permission\": \"viewer\",\n" +
                "  \"subject\": {\n" +
                "    \"object\": {\n" +
                "      \"objectType\": \"user\",\n" +
                "      \"objectId\": \"null\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            CheckPermissionRequestDTO checkPermissionRequestDTO = objectMapper.readValue(jsonStr, CheckPermissionRequestDTO.class);
            checkPermissionRequestDTO.getResource().setObjectType(objectType);
            checkPermissionRequestDTO.getResource().setObjectId(objectId);
            checkPermissionRequestDTO.getSubject().getObject().setObjectType(subjectType);
            checkPermissionRequestDTO.getSubject().getObject().setObjectId(subjectId);
            return checkPermissionRequestDTO;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }






    //由于updates的size限制在1000以内，于是进行拆分处理
    public void writeMultiLayerRelationships(int loopCount) {
        log.info("准备写入{}条权限数据", loopCount);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
        stopWatch.stop();
        log.info("写权限耗时cost time is {}", stopWatch.getTotalTimeMillis());
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
