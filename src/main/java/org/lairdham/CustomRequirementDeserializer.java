package org.lairdham;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lairdham.models.*;

import java.io.IOException;
import java.util.HashMap;

public class CustomRequirementDeserializer extends JsonDeserializer<Requirement> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Requirement deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        Requirement.RequirementType requirementType = Requirement.RequirementType.valueOf(jsonNode.get("type").asText());

        switch (requirementType) {
            case Rank:
                return getRankRequirement(jsonNode);

            case Attribute:
                return getAttributeRequirement(jsonNode);

            case Skill:
                return getSkillRequirement(jsonNode);

            case Edge:
                return getEdgeRequirement(jsonNode);

            default:
                return getDefaultRequirement(jsonParser);
        }

    }

    private Requirement<Rank> getRankRequirement(JsonNode jsonNode) {
        Requirement<Rank> requirement = new Requirement<>();
        requirement.setType(Requirement.RequirementType.Rank);
        requirement.setRequiredValue(Rank.valueOf(jsonNode.get("requiredValue").asText()));
        return requirement;
    }

    private Requirement<Attribute> getAttributeRequirement(JsonNode jsonNode) throws JsonProcessingException {
        Requirement<Attribute> requirement = new Requirement<>();
        requirement.setType(Requirement.RequirementType.Attribute);
        requirement.setRequiredValue(objectMapper.readValue(jsonNode.get("requiredValue").toString(), Attribute.class));
        return requirement;
    }

    private Requirement<Edge> getEdgeRequirement(JsonNode jsonNode) throws JsonProcessingException {
        Requirement<Edge> requirement = new Requirement<>();
        requirement.setType(Requirement.RequirementType.Edge);
        requirement.setRequiredValue(objectMapper.readValue(jsonNode.get("requiredValue").toString(), Edge.class));
        return requirement;
    }

    private Requirement<Skill> getSkillRequirement(JsonNode jsonNode) throws IOException {
        Requirement<Skill> requirement = new Requirement<>();
        requirement.setType(Requirement.RequirementType.Skill);
        requirement.setRequiredValue(objectMapper.readValue(jsonNode.get("requiredValue").toString(), Skill.class));
        return requirement;
    }

    private Requirement<HashMap<String, String>> getDefaultRequirement(JsonParser jsonParser) {
        return null;
    }

}
