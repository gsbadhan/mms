package mms.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;

@Configuration
public class ElasticsearchConfig extends org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration {
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String name;

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String host;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .build();
    }
}
