package br.com.battista.arcadia.caller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "br.com.battista.arcadia.caller.controller" })
public class AppConfig {
}
