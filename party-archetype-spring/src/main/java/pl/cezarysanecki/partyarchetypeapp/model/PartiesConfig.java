package pl.cezarysanecki.partyarchetypeapp.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PartiesConfig {

    @Bean
    PartiesFacade partiesFacade() {
        return new PartiesFacade(

        );
    }

}
