package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.DTO.permission.CheckPermissionRequestDTO;
import org.example.rpc.DTO.permission.CheckPermissionResponseDTO;
import org.example.rpc.DTO.relationship.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//测试单层级大数据量
@Slf4j
public class PerformanceTest1 extends BaseTest {

    @Test
    public void test10() {
        //写入10条关系数据
        commonWriteRelationships(10);
        //验证关系数量
        checkRelationshipsCount(10);
        //调用鉴权接口，查看鉴权性能
        checkPerformance();
    }

    @Test
    public void test100() {
        commonWriteRelationships(100);
        checkRelationshipsCount(100);
        checkPerformance();
    }


    @Test
    public void test500() {
        commonWriteRelationships(500);
        checkRelationshipsCount(500);
        checkPerformance();
    }

    @Test
    public void test1000() {
        commonWriteRelationships(1000);
        checkRelationshipsCount(1000);
        checkPerformance();
    }

    @Test
    public void test2000() {
        //由于writeRelationships接口入参update count限制在1000以内，分多次写入
        commonWriteRelationships(1000);
        commonWriteRelationships(1000);
        checkRelationshipsCount(2000);
        checkPerformance();
    }


    @Test
    public void test5000() {
        commonWriteRelationships(5000);
        checkRelationshipsCount(5000);
        checkPerformance();
    }

    @Test
    public void test10000() {
        commonWriteRelationships(10000);
        checkRelationshipsCount(10000);
        checkPerformance();
    }


    private void checkPerformance() {
        log.info("调用鉴权接口");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CheckPermissionResponseDTO checkPermissionResponseDTO = permissionFeignClient.checkPermission(token, generateCheckPermissionRequest(namespace+"role", "role_0", namespace+"user", "lx_0", "member"));
        stopWatch.stop();
        Assert.assertTrue(checkPermissionResponseDTO.getPermissionship().name().equals("PERMISSIONSHIP_HAS_PERMISSION"));
        log.info("cost time(ms) : {}", stopWatch.getTotalTimeMillis());
    }

    private void commonWriteRelationships(int loopCount) {
        log.info("准备写入{}条权限数据", loopCount);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int times = loopCount/1000;
        int remainder = loopCount%1000;
        for (int i=0;i<times;i++) {
            WriteRelationshipsRequestDTO requestDTO = generateWriteRelationshipsRequest(1000, i);
            WriteRelationshipsResponseDTO responseDTO = relationshipFeignClient.writeRelationships(token, requestDTO);
            Assert.assertTrue(StringUtils.isNotBlank(responseDTO.getWrittenAt().getToken()));
        }
        if (remainder > 0) {
            WriteRelationshipsRequestDTO requestDTO = generateWriteRelationshipsRequest(remainder, times);
            WriteRelationshipsResponseDTO responseDTO = relationshipFeignClient.writeRelationships(token, requestDTO);
            Assert.assertTrue(StringUtils.isNotBlank(responseDTO.getWrittenAt().getToken()));
        }
        stopWatch.stop();
        log.info("写权限耗时cost time is {}", stopWatch.getTotalTimeMillis());
    }



    private void checkRelationshipsCount(int loopCount) {
        String response = relationshipFeignClient.readRelationships(token, generateReadRelationshipsRequest());
        Assert.assertTrue(response != null && response.length() > 0);
        List<ReadRelationshipsResponseDTO> readRelationshipsResponseDTOS = HandleReadRelationshipsResult(response);
        Assert.assertTrue(!CollectionUtils.isEmpty(readRelationshipsResponseDTOS) && readRelationshipsResponseDTOS.size() == loopCount);
    }




    private CheckPermissionRequestDTO generateCheckPermissionRequest(String objectType, String objectId, String subjectType, String subjectId, String permission) {
        String jsonStr = "{\n" +
                "  \"consistency\": {\n" +
                "    \"fullyConsistent\": true\n" +
                "  },\n" +
                "  \"resource\": {\n" +
                "    \"objectType\": \"role\",\n" +
                "    \"objectId\": \"lx_role\"\n" +
                "  },\n" +
                "  \"permission\": \"member\",\n" +
                "  \"subject\": {\n" +
                "    \"object\": {\n" +
                "      \"objectType\": \"user\",\n" +
                "      \"objectId\": \"lx_0\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            CheckPermissionRequestDTO checkPermissionRequestDTO = objectMapper.readValue(jsonStr, CheckPermissionRequestDTO.class);
            checkPermissionRequestDTO.getResource().setObjectType(objectType);
            checkPermissionRequestDTO.getResource().setObjectId(objectId);
            checkPermissionRequestDTO.getSubject().getObject().setObjectType(subjectType);
            checkPermissionRequestDTO.getSubject().getObject().setObjectId(subjectId);
            checkPermissionRequestDTO.setPermission(permission);
            return checkPermissionRequestDTO;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private ReadRelationshipsRequestDTO generateReadRelationshipsRequest() {
        String jsonStr = "{\n" +
                "  \"resourceType\": \"role\",\n" +
                "  \"optionalResourceId\": \"role_0\",\n" +
                "  \"optionalRelation\": \"member\",\n" +
                "  \"optionalSubjectFilter\": {\n" +
                "    \"subjectType\": \"user\"\n" +
                "  }\n" +
                "}";
        try {
            RelationshipFilterDTO relationshipFilterDTO = objectMapper.readValue(jsonStr, RelationshipFilterDTO.class);
            relationshipFilterDTO.setResourceType(namespace.concat("role"));
            relationshipFilterDTO.setOptionalResourceId("role_0");
            relationshipFilterDTO.getOptionalSubjectFilter().setSubjectType(namespace.concat("user"));
            ReadRelationshipsRequestDTO requestDTO = new ReadRelationshipsRequestDTO();
            requestDTO.setRelationshipFilter(relationshipFilterDTO);
            ConsistencyDTO consistency = new ConsistencyDTO();
            consistency.setFullyConsistent(true); //关闭缓存，取最新数据
            requestDTO.setConsistency(consistency);
            return requestDTO;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }









}


