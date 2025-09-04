package com.spea.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(title = "SPEA API REST",
		version = "0.1",
		description = "API REST de precificação para empreendimentos alimentícios. "
				+ "Permite o cadastro de insumos, criação de receitas, associação entre insumos e receitas, "
				+ "e cálculo automático dos custos de produção.",
		contact = @Contact(
				name = "Rodrigo Pettenon Rodrigues",
				url = "https://github.com/rodrigopettenon",
				email = "rodrigopettenon.dev@gmail.com"),
		license = @License(
				name = "MIT License",
				url = "https://opensource.org/licenses/MIT"
		)),
		servers = @Server(url = "/", description = "Servidor Local")
)
@SpringBootApplication
public class SpeaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeaApiApplication.class, args);
	}

}
