package in.codifi.scrips.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class ApplicationProperties {

	/* Chat server config */
	@ConfigProperty(name = "config.app.ssh.host")
	private String sshHost;

	@ConfigProperty(name = "config.app.ssh.username")
	private String sshUserName;

	@ConfigProperty(name = "config.app.ssh.password")
	private String sshPassword;

	@ConfigProperty(name = "config.app.ssh.port")
	private int sshPort;

	@ConfigProperty(name = "config.app.ssh.file.path")
	private String remoteContractDire;

	@ConfigProperty(name = "config.app.local.file.path")
	private String localcontractDir;

	/* mtf local path */
	@ConfigProperty(name = "config.app.local.mtffile.path")
	private String localMtfDir;

	@ConfigProperty(name = "config.app.ssh.mtffile.path")
	private String remoteMtfDir;

	/* db values config */

	@ConfigProperty(name = "quarkus.datasource.username")
	private String dbUserName;

	@ConfigProperty(name = "quarkus.datasource.password")
	private String dbpassword;

	@ConfigProperty(name = "config.app.db.schema")
	private String dbSchema;

	/** stock return file paths **/

	@ConfigProperty(name = "config.app.local.stockfile.path")
	private String localStockDir;

	@ConfigProperty(name = "config.app.ssh.stockfile.path")
	private String remoteStockDire;

}
