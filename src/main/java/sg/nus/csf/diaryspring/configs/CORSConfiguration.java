package sg.nus.csf.diaryspring.configs;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CORSConfiguration implements WebMvcConfigurer{
    
    private String path;
    private String origins;

    public CORSConfiguration(String path, String origins) {
        // api paths
        this.path = path;
        // origin hosts
        this.origins = origins;
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
            .addMapping(this.path)
            .allowedOrigins(this.origins);
    }
}