package spring.app.modules.commons.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;

public class WebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        /*servletContext.setInitParameter(
                "spring.profiles.active", "test");*/
    }
}
