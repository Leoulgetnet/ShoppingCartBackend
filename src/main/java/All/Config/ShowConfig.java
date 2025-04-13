package All.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ShowConfig {
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
