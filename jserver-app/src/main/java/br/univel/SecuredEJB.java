package br.univel;

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

@Stateless
@SecurityDomain("form-auth")
@RolesAllowed({ "user_role" })
public class SecuredEJB {

	@Resource
	private SessionContext ctx;

	public String getSecurityInfo() {
		Principal principal = ctx.getCallerPrincipal();
		return "Seguro(Seja bem vindo " + principal.toString() + "! Acesso autorizado!)";
	}
}