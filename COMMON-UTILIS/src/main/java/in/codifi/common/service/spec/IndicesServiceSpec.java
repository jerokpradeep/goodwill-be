package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.IndicesMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface IndicesServiceSpec {

	RestResponse<GenericResponse> getIndices(ClinetInfoModel info);

	RestResponse<GenericResponse> addMappingScrips(List<IndicesMappingEntity> entities, ClinetInfoModel info);

	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	RestResponse<GenericResponse> getMappingScrips();

	RestResponse<GenericResponse> insertIndicesData();

}
