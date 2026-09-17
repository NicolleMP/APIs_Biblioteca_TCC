package br.edu.uri.biblioteca.soap.exception;


import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class SoapExceptionResolver extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, org.springframework.ws.soap.SoapFault fault) {
        fault.addFaultDetail()
                .addFaultDetailElement(
                        new QName("http://uri.edu.br/biblioteca/soap", "mensagem")
                )
                .addText(ex.getMessage());
    }
}
