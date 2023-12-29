package com.claro.amx.sp.tokenauthenticationservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@PropertySource(value = "${springdoc.config}", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "springdoc.info")
@Validated
public class SpringDocConfig {

    @NotNull
    @NotBlank
    private String nameContact;
    @NotNull
    @NotBlank
    private String mailContact;
    @NotNull
    @NotBlank
    private String urlContact;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String version;
    @NotNull
    @NotBlank
    private String urlConfluence;
    @NotNull
    @NotBlank
    @Value("${springdoc.enabledServerHttps}")
    private String enabledServerHttps;

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        if (isEnabled(this.enabledServerHttps))
            return openApi -> openApi.getServers().forEach(x -> {
                x.setUrl(x.getUrl().replace("http", "https"));
                x.setDescription(null);
            });
        return openApi -> {
        };
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name(nameContact)
                                .email(mailContact)
                                .url(urlContact)
                        ))
                .externalDocs(externalConfluenceDocumentation());
    }

    private boolean isEnabled(String value) {
        return (value != null && value.length() != 0 && value.compareTo("0") != 0);
    }

    private ExternalDocumentation externalConfluenceDocumentation() {
        if (isEnabled(this.urlConfluence))
            return new ExternalDocumentation().description("Confluence Documentation").url(urlConfluence);
        return null;
    }
}
