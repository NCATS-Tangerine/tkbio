package bio.knowledge.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Translator Knowledge.Bio API")
            .description("Documentation of the Translator Knowledge.Bio (TKBio) knowledge sources query web service application programming interfaces. Learn more about [TKBio](https://github.com/STARInformatics/tkbio) ")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("http://starinformatics.com")
            .version("4.0.2")
            .contact(new Contact("","", "richard@starinformatics.com"))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("bio.knowledge.server.api"))
                    .build()
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

}
