package in.codifi.admin.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.config.ApplicationProperties;
import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.FormDataModel;
import in.codifi.admin.req.model.UserRoleMapReqModel;
import in.codifi.admin.service.spec.ClientServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;
import in.codifi.admin.ws.model.kc.CreateUserCredentialsModel;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;
import in.codifi.admin.ws.model.kc.UserAttribute;
import in.codifi.admin.ws.service.kc.KeyCloakAdminRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ClientService implements ClientServiceSpec {

	@Inject
	ApplicationProperties props;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	KeyCloakAdminRestService keyCloakAdminRestService;
	@Inject
	KeyCloakConfig keycloakConfig;

	/**
	 * Method to create existing user in keycloak
	 * 
	 * @author Dinesh Kumar
	 * @param file
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createUsers() {
		try {
			File folder = new File(props.getClientFilePath());
			File[] listOfFiles = folder.listFiles();
			for (int count = 0; count < listOfFiles.length; count++) {
				File file = listOfFiles[count];
				if (file.isFile() && file.getName().endsWith(AppConstants.TEXT_FILE_FORMATS)) {
					prepareUserCreation(file);
				}
			}
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * method to file move
	 * 
	 * @author SOWMIYA
	 * @param createUser
	 * @param file
	 * @param fileName
	 * @throws IOException
	 */
	private void FileMove(List<String> createUser, File file, String fileName) throws IOException {
		String date = new SimpleDateFormat("ddMMYY-hhmm").format(new Date());
		String responseFileName = props.getClientResultPath() + fileName + date + AppConstants.TEXT_FILE_FORMATS;
		File fileDir = new File(props.getClientResultPath());
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		Path filePath = Path.of(responseFileName);
		BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE);
		for (String data : createUser) {
			writer.write(data);
			writer.newLine();
		}
		writer.close();

		String strDate = new SimpleDateFormat("ddMMYYYY").format(new Date());
		int size = props.getClientCompletedPath().lastIndexOf("/");
		String slash = "//";
		if (size > 0) {
			slash = "/";
		}
		File completed = new File(props.getClientCompletedPath() + strDate);
		if (!completed.exists()) {
			completed.mkdirs();
		}
		System.out.println(completed.toString() + slash + date);
		if (file.renameTo(new File(completed.toString() + slash + date))) {
			file.delete();
			Log.info("File Moved Successfully");
		}

	}

	/**
	 * method to insert user into key coack
	 * 
	 * @author SOWMIYA
	 * 
	 * @param requestModel
	 * @return
	 */
	public List<String> insertUser(List<CreateUserRequestModel> requestModel, String role) {

		List<Callable<String>> tasks = new ArrayList<>();
		for (CreateUserRequestModel model : requestModel) {
			Callable<String> task = () -> {
				String message = keyCloakAdminRestService.addNewUser(model);

				if (message.equals("User Created")) {
					if (role.equalsIgnoreCase("Active")) {
						activeUserRoleMapping(model.getUsername());
					} else {
						dormatUserRoleMapping(model.getUsername());
					}
					return "User Created - " + model.getUsername();
				} else if (message.equals("User already exists")) {
					return "User already exists - " + model.getUsername();
				}
				return "Failed to Created - " + model.getUsername();
			};
			tasks.add(task);
		}
		return tasks.stream().map(callable -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	/**
	 * Method to prepare request to create user from TXT file
	 * 
	 * @author Dinesh Kumar
	 * @param file
	 * @return
	 */
	private void prepareUserCreation(File file) throws FileNotFoundException {
		List<CreateUserRequestModel> activeList = new ArrayList<>();
		List<CreateUserRequestModel> dormatList = new ArrayList<>();
		String fName = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String strLine = "";
		try {
			br.readLine();
			while ((strLine = br.readLine()) != null) {
				String[] values = strLine.trim().split("\\|");
				CreateUserRequestModel requestModel = new CreateUserRequestModel();
				if (values.length == 10) {
					List<CreateUserCredentialsModel> userCredentilList = new ArrayList<>();
					UserAttribute attribute = new UserAttribute();
					requestModel.setUsername(values[0]);
					String firstName = "";
					if (StringUtil.isNullOrEmpty(values[2])) {
						firstName = values[0];
					} else {
						firstName = values[2];
					}
					if (values[1] == null || values[1].isEmpty()) {
						if (values[7].contains("MARRIED") && values[8].contains("F")) {
							fName = "MRS" + " " + firstName;
							requestModel.setFirstName(fName);
						} else if (values[7].contains("SINGLE") && values[8].contains("F")) {
							fName = "MS" + " " + firstName;
							requestModel.setFirstName(fName);
						} else if (values[8].contains("M")) {
							fName = "MR" + " " + firstName;
							requestModel.setFirstName(fName);
						}
					} else {
						requestModel.setFirstName(values[1] + " " + firstName);
					}
					requestModel.setLastName(values[3]);
					attribute.setMobile(values[4]);
					requestModel.setEmail(values[5]);
					requestModel.setEnabled(true);
					requestModel.setEmailVerified(true);
					attribute.setUcc(values[0]);
					attribute.setPan(values[6]);
					attribute.setMaritalStatus(values[7]);
					attribute.setGender(values[8]);
					requestModel.setAttributes(attribute);
					CreateUserCredentialsModel credentialsModel = new CreateUserCredentialsModel();
					credentialsModel.setType("password");
					credentialsModel.setValue("Abc@1234");
					userCredentilList.add(credentialsModel);
					requestModel.setCredentials(userCredentilList);
					if (values[9].contains("Active")) {
						activeList.add(requestModel);
					} else if (values[9].contains("Dormant")) {
						dormatList.add(requestModel);
					}
				}
			}
			if (StringUtil.isListNotNullOrEmpty(activeList)) {
				String activeUserFileName = "ActiveUser";
				List<String> createActiveUserIntoKeycoack = insertUser(activeList, "Active");
				FileMove(createActiveUserIntoKeycoack, file, activeUserFileName);
			}
			if (StringUtil.isListNotNullOrEmpty(dormatList)) {
				String dormatUserFileName = "DormatUser";
				List<String> createDormatUserIntoKeycoack = insertUser(dormatList, "Dormant");
				FileMove(createDormatUserIntoKeycoack, file, dormatUserFileName);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	/**
	 * method to active user role mapping
	 * 
	 * @author SOWMIYA
	 * @param userId
	 */
	public void activeUserRoleMapping(String userId) {
		try {
			List<UserRoleMapReqModel> mapReqModels = new ArrayList<>();
			UserRoleMapReqModel model = new UserRoleMapReqModel();
			model.setId(keycloakConfig.getActiveRoleId());
			model.setName(keycloakConfig.getActiveRoleName());
			mapReqModels.add(model);
			String message = keyCloakAdminRestService.userRoleMapping(mapReqModels, userId,
					keycloakConfig.getClientCholaId());
			if (StringUtil.isNotNullOrEmpty(message)) {
				Log.info("Role Mapping - " + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	/**
	 * method to dormat user role mapping
	 * 
	 * @author SOWMIYA
	 * @param userId
	 */
	public void dormatUserRoleMapping(String userId) {
		try {
			List<UserRoleMapReqModel> mapReqModels = new ArrayList<>();
			UserRoleMapReqModel model = new UserRoleMapReqModel();
			model.setId(keycloakConfig.getDormantRoleId());
			model.setName(keycloakConfig.getDormatRoleName());
			mapReqModels.add(model);
			String message = keyCloakAdminRestService.userRoleMapping(mapReqModels, userId,
					keycloakConfig.getClientCholaId());
			if (StringUtil.isNotNullOrEmpty(message)) {
				Log.info("Role Mapping - " + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	/**
	 * method to upload client details
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> uploadClientDetails(FormDataModel file) {
		try {
			/** check parameters **/
			if (file.getFile() == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			/** to get the file name **/
			String fileName = file.getFile().fileName();
			int dotIndex = fileName.lastIndexOf(".");
			String extension = "";
			if (dotIndex > 0) {
				extension = fileName.substring(dotIndex);
			}
			if (fileName == null || !fileName.endsWith(extension))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_FILE_TYPE);

			/** to make file path **/
			String filePath = props.getClientFilePath() + "clientMaster" + AppConstants.TEXT_FILE_FORMATS;
			File fileDir = new File(props.getClientFilePath());
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			/** upload a file **/
			Path targetPath = Paths.get(filePath);
			Files.deleteIfExists(targetPath);
			Files.copy(file.getFile().filePath(), targetPath);

			return prepareResponse.prepareSuccessMessage(AppConstants.FILE_UPLOADED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

}
