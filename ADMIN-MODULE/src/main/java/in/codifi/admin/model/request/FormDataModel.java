
package in.codifi.admin.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormDataModel {

	@RestForm("file")
	@Valid
	@NotNull
	private FileUpload file;

	@FormParam(value = "type")
	private String type;

	@FormParam(value = "exchange")
	private String exchange;

}
