package org.revo.auth.Config;

/**
 * Created by ashraf on 17/04/17.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig
        implements WebMvcConfigurer
//        extends WebMvcConfigurerAdapter
{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/signin").setViewName("signin");
        registry.addViewController("/done").setViewName("done");
    }
}