package com.stever.gmall.search.conf;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JestClientFactoryConf {
    @Bean
    public JestClientFactory jestClientFactory(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder("http://192.168.137.60:9200")
                        .multiThreaded(false)
                        .defaultMaxTotalConnectionPerRoute(1)
                        .maxTotalConnection(1)
                        .build());
        return factory;
    }
}
