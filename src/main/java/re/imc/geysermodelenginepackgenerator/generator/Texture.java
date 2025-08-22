package re.imc.geysermodelenginepackgenerator.generator;

import java.util.Set;

public class Texture {

    private final String modelId;
    private final String path;
    private final Set<String> bindingBones;
    private final byte[] image;

    public Texture(String modelId, String path, Set<String> bindingBones, byte[] image) {
        this.modelId = modelId;
        this.path = path;
        this.bindingBones = bindingBones;
        this.image = image;
    }

    public String getModelId() {
        return modelId;
    }

    public String getPath() {
        return path;
    }

    public Set<String> getBindingBones() {
        return bindingBones;
    }

    public byte[] getImage() {
        return image;
    }
}
