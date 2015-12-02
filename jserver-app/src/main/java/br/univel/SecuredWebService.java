package br.univel;

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

@Stateless
@SecurityDomain("form-auth")
@PermitAll
@WebService
@WebContext(authMethod = "BASIC", secureWSDLAccess = false)
public class SecuredWebService {

	@Resource
	private SessionContext ctx;

	@EJB
	private SecuredEJBLocal securedEJB;

	public String sayHello() {

		Principal principal = ctx.getCallerPrincipal();

		String securityInfoEjb;
		try {
			securityInfoEjb = securedEJB.getHello();
		} catch (EJBAccessException ejbAccessException) {
			securityInfoEjb = "O usuário " 
						+ principal.toString() 
						+ " não tem acesso ao EJB.";
		}

		return "Webservice "+ principal.toString() + "(" + securityInfoEjb + ")";
	}
}
