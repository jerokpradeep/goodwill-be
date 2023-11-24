package in.codifi.common.ws.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class HotPursuitScripDataDeserializer extends JsonDeserializer<HotPursuitScripData> {
    @Override
    public HotPursuitScripData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();

        if (node.isTextual() && node.textValue().equals("-")) {
            return null; // Return null for the value "-"
        }

        HotPursuitScripData scripData = new HotPursuitScripData();
        scripData.setCompanyCode(node.get("CompanyCode").asText());
        scripData.setMktSegmentId(node.get("MarketSegmentId").asText());
        scripData.setODINCode(node.get("ODINCode").asText());
        scripData.setSymbol(node.get("Symbol").asText());
        scripData.setSeries(node.get("Series").asText());

        return scripData;
    }
}

