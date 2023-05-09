package org.example.web;

import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass;
import com.authzed.api.v1alpha1.Schema;
import com.authzed.grpcutil.BearerToken;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SchemaRPCTest {
    ManagedChannel channel = ManagedChannelBuilder
            .forTarget("grpc.authzed.com:3332")
            .useTransportSecurity()
            .build();

    BearerToken bearerToken = new BearerToken("lx_token");
    SchemaServiceGrpc.SchemaServiceBlockingStub schemaService = SchemaServiceGrpc.newBlockingStub(channel)
            .withCallCredentials(bearerToken);
    @Test
    public void testWriteSchema() {
        String schema = "definition blog/user {}\n" +
                "                            \n" +
                "            definition blog/post {\n" +
                "                relation reader: blog/user\n" +
                "                relation writer: blog/user\n" +
                "                            \n" +
                "                permission read = reader + writer\n" +
                "                permission write = writer\n" +
                "            }";
        SchemaServiceOuterClass.WriteSchemaRequest request = SchemaServiceOuterClass.WriteSchemaRequest
                .newBuilder()
                .setSchema(schema)
                .build();

        SchemaServiceOuterClass.WriteSchemaResponse response;
        try {
            response = schemaService.writeSchema(request);
        } catch (Exception e) {
            // Uh oh!
            log.error("erorr, msg is {}", e.getMessage(), e);
        }
    }


    @Test
    public void testReadSchema() {
        SchemaServiceOuterClass.ReadSchemaRequest request = SchemaServiceOuterClass.ReadSchemaRequest.newBuilder().build();
        try {
            SchemaServiceOuterClass.ReadSchemaResponse response = schemaService.readSchema(request);
            String schemaText = response.getSchemaText();
            System.out.println(schemaText);
        } catch (Exception e) {
            log.error("read schema error, msg is {}", e.getMessage(), e);
        }

    }

}
