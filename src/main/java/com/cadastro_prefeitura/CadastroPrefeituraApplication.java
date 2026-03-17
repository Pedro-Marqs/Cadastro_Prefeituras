package com.cadastro_prefeitura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com")
@EntityScan(basePackages = {"com.domains","com.domains.enums"})
@EnableJpaRepositories(basePackages = "com.repositories")
@SpringBootApplication
public class CadastroPrefeituraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CadastroPrefeituraApplication.class, args);
    }

}
