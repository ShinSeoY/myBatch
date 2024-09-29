//package com.my.batch.common.elasticsearch;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.my.batch.elasticsearchRepository")
//public class ElasticsearchConfig extends ElasticsearchConfiguration {
//
//    @Value("${spring.elasticsearch.uris}")
//    private String elasticsearchUrl;
//
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedTo(elasticsearchUrl.replace("http://", ""))
//                .build();
//    }
//}