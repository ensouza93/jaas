
package br.univel.artefatos;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SecuredWebService", targetNamespace = "http://univel.br/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SecuredWebService {


    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://univel.br/", className = "br.univel.artefatos.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://univel.br/", className = "br.univel.artefatos.SayHelloResponse")
    public String sayHello();

}
