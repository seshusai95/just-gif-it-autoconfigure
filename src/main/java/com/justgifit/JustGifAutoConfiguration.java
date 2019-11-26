package com.justgifit;

import com.justgifit.services.ConverterService;
import com.justgifit.services.GifEncoderService;
import com.justgifit.services.VideoDecoderService;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@ConditionalOnClass(value = {FrameGrabber.class, AnimatedGifEncoder.class})
/*Only apply this configuration if the class is present on the class path*/ public class JustGifAutoConfiguration {

    @Autowired
    private JustGifItProperties justGifItProperties;


    @Bean
    @ConditionalOnProperty(prefix = "com.justgifit", name = "create-result-dir")
    public boolean createResultDir() {
        if (!justGifItProperties.getGifLocation().exists()) {
            justGifItProperties.getGifLocation().mkdir();
        }
        return true;
    }

    @Bean
    @ConditionalOnMissingBean(value = {VideoDecoderService.class})
    /*Only apply this configuration if the bean of that particular class is not already contained in the Application Context*/ public VideoDecoderService videoDecoderService() {
        return new VideoDecoderService();
    }

    @Bean
    @ConditionalOnMissingBean(value = {GifEncoderService.class})
    /*Only apply this configuration if the bean of that particular class is not already contained in the Application Context*/ public GifEncoderService gifEncoderService() {
        return new GifEncoderService();
    }

    @Bean
    @ConditionalOnMissingBean(value = {ConverterService.class})
    /*Only apply this configuration if the bean of that particular class is not already contained in the Application Context*/ public ConverterService converterService() {
        return new ConverterService();
    }


    @Configuration
    @ConditionalOnWebApplication
    /*Only apply this configuration if the Application Context is a WebApplication Context*/ public static class WebConfiguration {

        @Value("${multipart.location}/gif/")
        private String gifLocation;

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterHiddenHttpMethodFilter(HiddenHttpMethodFilter filter) {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
            filterRegistrationBean.setEnabled(false);
            return filterRegistrationBean;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterHttpPutFormContentFilter(HttpPutFormContentFilter filter) {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
            filterRegistrationBean.setEnabled(false);
            return filterRegistrationBean;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterRequestContextFilter(RequestContextFilter filter) {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
            filterRegistrationBean.setEnabled(false);
            return filterRegistrationBean;
        }

        @Bean
        public WebMvcConfigurer webMvcConfigurer() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addResourceHandlers(ResourceHandlerRegistry registry) {
                    registry.addResourceHandler("/gif/**").addResourceLocations("file:" + gifLocation);
                    super.addResourceHandlers(registry);
                }
            };
        }

    }


}
