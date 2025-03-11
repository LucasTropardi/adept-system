package com.ltsoftwaresupport.adept;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@Theme(value = "adept-system")
@PWA(name = "Adept-System",
        shortName = "Adept",
        iconPath = "icons/icon.png")
public class AdeptApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        // Define o fuso horário padrão
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        // Define dinamicamente as configurações do banco de dados antes do Spring iniciar
        SpringApplication app = new SpringApplication(AdeptApplication.class);
        app.addListeners(new DatabaseConfigLoader());
        app.run(args);
    }

    // Classe interna para carregar as propriedades do banco dinamicamente
    private static class DatabaseConfigLoader implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

        @Override
        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
            ConfigurableEnvironment environment = event.getEnvironment();
            Properties properties = new Properties();

            // Definição das localizações do arquivo de configuração
            String[] possibleLocations = {
                    ".data/database.properties",  // Caminho para produção (oculto na raiz do sistema)
                    "C:\\data\\database.properties" // Caminho para desenvolvimento (Windows)
            };

            for (String location : possibleLocations) {
                File file = new File(location);
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        properties.load(fis);
                        // Carregar as propriedades no ambiente do Spring
                        properties.forEach((key, value) -> {
                            environment.getSystemProperties().put(key.toString(), value.toString());
                        });
                        System.out.println("Configurações do banco carregadas de: " + location);
                        return; // Se encontrar um arquivo válido, para a busca
                    } catch (IOException e) {
                        System.err.println("Erro ao carregar propriedades do banco: " + e.getMessage());
                    }
                }
            }
            System.err.println("Nenhum arquivo de configuração do banco encontrado. Usando configurações padrão.");
        }
    }
}
