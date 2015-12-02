package br.univel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.jboss.security.ClientLoginModule;
import org.jboss.security.SecurityContextAssociation;

public class TesteEjbRemoto {

	private static final String PORTA = "8080";
	private static final String ENDERECO = "localhost";
	private final static String configurationName = "teste";

	private void performTestingClientLoginModule(final String user, final String password) throws Exception {

		System.out.println("Testes: user = " + user + ", password = " + password);

		LoginContext loginContext = getCLMLoginContext(user, password);

		try {
			loginContext.login();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			SecuredEJBRemote ejbRemoto = lookupSecuredEJB();
			System.out.println(ejbRemoto.getHello());
		} finally {
			// o ejbremoto ainda vai funcionar, mas novas instancias não.
			loginContext.logout();
			System.out.println("logout");
		}
	}

	private LoginContext getCLMLoginContext(final String username, final String password) throws LoginException {

		CallbackHandler callbackHandler = new CallbackHandler() {
			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

				for (Callback callback : callbacks) {
					if (callback instanceof NameCallback) {
						((NameCallback) callback).setName(username);

					} else if (callback instanceof PasswordCallback) {
						((PasswordCallback) callback).setPassword(password.toCharArray());

					} else {
						throw new UnsupportedCallbackException(callback);
					}
				}
			}
		};

		Configuration configuration = new Configuration() {

			@Override
			public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

				if (!configurationName.equals(name)) {
					throw new IllegalArgumentException("Unexpected configuration name '" + name + "'");
				}

				Map<String, String> options = new HashMap<String, String>();
				options.put("multi-threaded", "true");
				options.put("restore-login-identity", "true");

				AppConfigurationEntry clmEntry = new AppConfigurationEntry(ClientLoginModule.class.getName(),
						AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

				return new AppConfigurationEntry[] { clmEntry };
			}
		};
		return new LoginContext(configurationName, new Subject(), callbackHandler, configuration);
	}

	private SecuredEJBRemote lookupSecuredEJB() throws Exception {

		Properties pr = new Properties();

		pr.put("remote.connections", "default");
		
		pr.put("remote.connection.default.host", ENDERECO);
		pr.put("remote.connection.default.port", PORTA);
		
		pr.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		pr.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		pr.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		pr.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS",
				"JBOSS-LOCAL-USER");
		
		String usuario = SecurityContextAssociation.getPrincipal().getName();
		pr.put("remote.connection.default.username", usuario);
		
		String senha = new String((char[]) SecurityContextAssociation.getCredential());
		pr.put("remote.connection.default.password", senha);
		
		PropertiesBasedEJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(pr);

		ContextSelector<EJBClientContext> ejbClientContextSelector = new ConfigBasedEJBClientContextSelector(cc);

		EJBClientContext.setSelector(ejbClientContextSelector);

		StatelessEJBLocator<SecuredEJBRemote> locator = new StatelessEJBLocator<SecuredEJBRemote>(
				SecuredEJBRemote.class, "", "jserver-app", "SecuredEJBDelegate", "");

		return org.jboss.ejb.client.EJBClient.createProxy(locator);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("\n\nTestes com senha correta\n\n");
		new TesteEjbRemoto().performTestingClientLoginModule("user", "user");

		System.out.println("\n\nTestes com senha errada\n\n");
		new TesteEjbRemoto().performTestingClientLoginModule("user", "pass");

	}
}
