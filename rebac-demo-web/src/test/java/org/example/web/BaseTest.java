package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.DTO.relationship.ReadRelationshipsResponseDTO;
import org.example.rpc.DTO.relationship.RelationshipUpdateDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsRequestDTO;
import org.example.rpc.DTO.schema.ReadSchemaResponseDTO;
import org.example.rpc.DTO.schema.WriteSchemaRequestDTO;
import org.example.rpc.PermissionFeignClient;
import org.example.rpc.RelationshipFeignClient;
import org.example.rpc.SchemaFeignClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {

    @Autowired
    protected SchemaFeignClient schemaFeignClient;

    @Autowired
    protected RelationshipFeignClient relationshipFeignClient;

    @Autowired
    protected PermissionFeignClient permissionFeignClient;

    protected String token = "Bearer lx_token";

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected String namespace = "namespace_" + System.currentTimeMillis() + "_";


    @Before
    public void before() {
        log.info("执行schema写入，namespace is {}", namespace);
        ReadSchemaResponseDTO responseDTO = schemaFeignClient.readSchema(token);
        schemaFeignClient.writeSchema(token, generateWriteSchemaRequest(responseDTO.getSchemaText() + generateSchema()));
    }


    //接口为流api，需单独处理
    protected List<ReadRelationshipsResponseDTO> HandleReadRelationshipsResult(String response) {
        try {
            String[] splits = response.split("\\{\"result\":");
            List<ReadRelationshipsResponseDTO> results = new ArrayList<>();
            for (String split : splits) {
                if (StringUtils.isBlank(split)) continue;
                ReadRelationshipsResponseDTO responseDTO = objectMapper.readValue("{\"result\":".concat(split), ReadRelationshipsResponseDTO.class);
                results.add(responseDTO);
            }
            return results;
        } catch (Exception e) {
            log.error("error msg is {}", e.getMessage(), e);
        }
        return null;
    }


    private WriteSchemaRequestDTO generateWriteSchemaRequest(String schema) {
        WriteSchemaRequestDTO requestDTO = new WriteSchemaRequestDTO();
        requestDTO.setSchema(schema);
        return requestDTO;
    }


    private String generateSchema() {
        StringBuffer sb = new StringBuffer();

        sb.append("definition ").append(namespace).append("user {} ");
        sb.append("definition ").append(namespace).append("role { relation member:").append(namespace).append("user \n").append("relation parent: ").append(namespace).append("role \n").append("relation child: ").append(namespace).append("role \n").append("permission viewer= member + child->viewer \n").append("}");
        sb.append("definition ").append(namespace).append("resource {relation reader:").append(namespace).append("role#member \n").append("relation reader_role: ").append(namespace).append("role \n").append("permission read=reader + reader_role->viewer \n").append("}");

        return sb.toString();

    }


    /**
     * 生成单层多个relationships授权数据
     * （由于updates有size限制，对于大数据量，分次执行）
     */
    protected WriteRelationshipsRequestDTO generateWriteRelationshipsRequest(int loopCount, int times) {
        List<RelationshipUpdateDTO> updates = new ArrayList<>();
        for (int i = times * loopCount; i < (times + 1) * loopCount; i++) {
            String jsonStr = "{\n" +
                    "      \"operation\": \"OPERATION_CREATE\",\n" +
                    "      \"relationship\": {\n" +
                    "        \"resource\": {\n" +
                    "          \"objectType\": \"role\",\n" +
                    "          \"objectId\": \"role_0\"\n" +
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
                relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectType(namespace + "user");
                relationshipUpdateDTO.getRelationship().getResource().setObjectType(namespace + "role");
                relationshipUpdateDTO.getRelationship().getSubject().getObject().setObjectId("lx_" + i);
                updates.add(relationshipUpdateDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        WriteRelationshipsRequestDTO requestDTO = new WriteRelationshipsRequestDTO();
        requestDTO.setUpdates(updates);
        return requestDTO;
    }


}
