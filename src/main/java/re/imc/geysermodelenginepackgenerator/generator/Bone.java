package re.imc.geysermodelenginepackgenerator.generator;

import java.util.Set;

public class Bone {

   private final String name;
   private final String parent;
   private final Set<Bone> children;
   private final Set<Bone> allChildren;

   public Bone(String name, String parent, Set<Bone> children, Set<Bone> allChildren) {
       this.name = name;
       this.parent = parent;
       this.children = children;
       this.allChildren = allChildren;
   }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public Set<Bone> getChildren() {
        return children;
    }

    public Set<Bone> getAllChildren() {
        return allChildren;
    }
}
