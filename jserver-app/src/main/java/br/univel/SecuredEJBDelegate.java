package br.univel;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class SecuredEJBDelegate implements SecuredEJBRemote, SecuredEJBLocal {

	@EJB
	private SecuredEJB securedEJB;

	@Override
	public String getHello() {
		return "Inseguro chamando(" + securedEJB.getSecurityInfo() + ")";
	}
}
