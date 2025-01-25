package com.oimc.aimin.es.config;

import com.oimc.aimin.es.init.StartupEventListener;
import com.oimc.aimin.es.service.DrugEsOperationImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Import({
        StartupEventListener.class,
        DrugEsOperationImpl.class
})
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.oimc.aimin.es.repository")
public class EsAutoConfiguration {
}
