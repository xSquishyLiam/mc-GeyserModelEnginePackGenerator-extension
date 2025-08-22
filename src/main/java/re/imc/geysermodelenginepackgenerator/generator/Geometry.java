package re.imc.geysermodelenginepackgenerator.generator;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

public class Geometry {

    private String modelId;
    private String geometryId;
    private JsonObject json;
    private Map<String, Bone> bones = new HashMap<>();

    private String path;

    public void load(String json) {
        this.json = JsonParser.parseString(json).getAsJsonObject();
    }
    public void setId(String id) {
        geometryId = id;
        getInternal().get("description").getAsJsonObject().addProperty("identifier", id);
    }

    public void setTextureWidth(int w) {
        getInternal().get("description").getAsJsonObject().addProperty("texture_width", w);
    }

    public void setTextureHeight(int h) {
        getInternal().get("description").getAsJsonObject().addProperty("texture_height", h);
    }

    public JsonObject getInternal() {
        return json.get("minecraft:geometry").getAsJsonArray().get(0)
                .getAsJsonObject();
    }

    public void modify() {
        JsonArray array = getInternal().get("bones").getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            if (element.isJsonObject()) {
                String name = element.getAsJsonObject().get("name").getAsString().toLowerCase(Locale.ROOT);

                String parent = element.getAsJsonObject().has("parent") ? element.getAsJsonObject().get("parent").getAsString().toLowerCase() : null;
                element.getAsJsonObject().remove("name");
                element.getAsJsonObject().addProperty("name", name);

                if (name.equals("hitbox") || name.equals("shadow") || name.equals("mount") || name.startsWith("b_") || name.startsWith("ob_")) {
                    iterator.remove();
                } else {
                    bones.put(name, new Bone(name, parent, new HashSet<>(), new HashSet<>()));
                }
            }

            for (Bone bone : bones.values()) {
                if (bone.getParent() != null) {
                    Bone parent = bones.get(bone.getParent());
                    if (parent != null) {
                        parent.getChildren().add(bone);
                        addAllChildren(parent, bone);
                    }
                }
            }
        }
        setId("geometry.meg_" + modelId);
    }

    public void addAllChildren(Bone p, Bone c) {
        p.getAllChildren().add(c);
        Bone parent = bones.get(p.getParent());
        if (parent != null) {
            addAllChildren(parent, c);
        }
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setGeometryId(String geometryId) {
        this.geometryId = geometryId;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModelId() {
        return modelId;
    }

    public String getGeometryId() {
        return geometryId;
    }

    public JsonObject getJson() {
        return json;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Bone> getBones() {
        return bones;
    }
}
