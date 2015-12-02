package br.univel;

import javax.ejb.Remote;

@Remote
public interface SecuredEJBRemote {
	public String getHello();
}