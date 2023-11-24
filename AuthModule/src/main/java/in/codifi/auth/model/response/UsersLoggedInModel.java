package in.codifi.auth.model.response;

import java.util.List;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersLoggedInModel {
	private long web;
	private long mob;
	private long api;
	private List<JSONObject> sso;

}
