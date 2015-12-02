package br.univel.jaas;

import java.security.acl.Group;

import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

public class LoginModule extends UsernamePasswordLoginModule {

	@Override
	public boolean login() throws LoginException {
		return super.login();
	}

	@Override
	protected String getUsersPassword() throws LoginException {
		String username = super.getUsername();

		System.out.format("Autenticando o Uuário '%s'.\n", username);

		String password = username;

		password = password.toUpperCase();

		return password;
	}

	@Override
	protected boolean validatePassword(String inputPassword,
			String expectedPassword) {
		String encryptedInputPassword = (inputPassword == null) ? null
				: inputPassword.toUpperCase();

		System.out.format(
				"Validando o password como parametro decriptado '%s' "
						+ "com o password '%s' válido para esse usuário;\n",
				encryptedInputPassword, expectedPassword);

		return super.validatePassword(encryptedInputPassword, expectedPassword);
	}

	@Override
	protected Group[] getRoleSets() throws LoginException {
		SimpleGroup group = new SimpleGroup("Roles");
		group.addMember(new SimplePrincipal("user_role"));
		return new Group[] { group };
	}

}
