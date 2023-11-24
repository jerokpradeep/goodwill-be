package in.codifi.common.ws.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class AnnoucementsScripDataDeserializer extends JsonDeserializer<AnnoucementsScripDataRestResp> {
    @Override
    public AnnoucementsScripDataRestResp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();

        if (node.isTextual() && node.textValue().equals("-")) {
            return null; // Return null for the value "-"
        }

        AnnoucementsScripDataRestResp scripData = new AnnoucementsScripDataRestResp();
        scripData.setCompanyCode(node.get("CompanyCode").asInt());
        scripData.setMktSegmentId(node.get("MarketSegmentId").asInt());
        scripData.setODINCode(node.get("ODINCode").asInt());
        scripData.setSymbol(node.get("Symbol").asText());
        scripData.setSeries(node.get("Series").asText());

        return scripData;
    }
}