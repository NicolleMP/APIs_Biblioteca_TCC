package br.edu.uri.biblioteca.soap.config;

import br.edu.uri.biblioteca.exception.IsbnDuplicadoException;
import br.edu.uri.biblioteca.soap.exception.SoapExceptionResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "biblioteca")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema bibliotecaSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("BibliotecaPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://uri.edu.br/biblioteca/soap");
        wsdl11Definition.setSchema(bibliotecaSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema bibliotecaSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/biblioteca.xsd"));
    }

    @Bean
    public SoapExceptionResolver soapExceptionResolver() {
        SoapExceptionResolver resolver = new SoapExceptionResolver();

        Properties mappings = new Properties();

        mappings.setProperty(
                IsbnDuplicadoException.class.getName(),
                "CLIENT"
        );

        mappings.setProperty(
                java.util.NoSuchElementException.class.getName(),
                "CLIENT"
        );

        resolver.setExceptionMappings(mappings);

        return resolver;
    }
}