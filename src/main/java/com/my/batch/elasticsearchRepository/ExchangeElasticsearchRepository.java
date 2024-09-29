//package com.my.batch.elasticsearchRepository;
//
//import com.my.batch.domain.Exchange;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//import java.util.List;
//
//public interface ExchangeElasticsearchRepository extends ElasticsearchRepository<Exchange, String> {
//    Page<Exchange> findByNameContaining(String name, Pageable pageable);
//    List<Exchange> findByNameContaining(String name);
//}