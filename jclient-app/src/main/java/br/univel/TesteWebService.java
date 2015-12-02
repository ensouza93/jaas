package br.univel;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import br.univel.artefatos.SecuredWebService;
import br.univel.artefatos.SecuredWebServiceService;

public class TesteWebService {

	private static final String USER_NAME = "user";
	private static final String PASSWORD = "user";

	public static void main(String[] args) {

		SecuredWebServiceService s = new SecuredWebServiceService();
		SecuredWebService port = s.getSecuredWebServicePort();

		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER_NAME, PASSWORD.toCharArray());
			}
		});

		String str = port.sayHello();
		System.out.println(str);
	}

}
