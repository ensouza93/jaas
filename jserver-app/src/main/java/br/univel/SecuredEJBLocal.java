package br.univel;

import javax.ejb.Local;

@Local
public interface SecuredEJBLocal {
	public String getHello();
}