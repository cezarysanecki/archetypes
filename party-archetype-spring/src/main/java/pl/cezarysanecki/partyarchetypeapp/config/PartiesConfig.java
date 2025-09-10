package pl.cezarysanecki.partyarchetypeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;

@Configuration
public class PartiesConfig {

    @Bean
    PartiesFacade partiesFacade() {
        return new PartiesFacade(

        );
    }

}
