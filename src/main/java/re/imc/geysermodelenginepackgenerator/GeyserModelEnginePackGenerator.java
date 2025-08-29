package re.imc.geysermodelenginepackgenerator;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineResourcePacksEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;
import re.imc.geysermodelenginepackgenerator.managers.ConfigManager;
import re.imc.geysermodelenginepackgenerator.managers.resourcepack.ResourcePackManager;

public class GeyserModelEnginePackGenerator implements Extension {

    private static GeyserModelEnginePackGenerator extension;

    private ConfigManager configManager;
    private ResourcePackManager resourcePackManager;

    @Subscribe
    public void onLoad(GeyserPreInitializeEvent event) {
        extension = this;

        loadManagers();

        resourcePackManager.loadPack();
    }

    @Subscribe
    public void onDefineCommand(GeyserDefineCommandsEvent event) {
        event.register(Command.builder(this)
                .name("reload")
                .source(CommandSource.class)
                .playerOnly(false)
                .description("GeyserModelPackGenerator Reload Command")
                .permission("geysermodelenginepackgenerator.admin")
                .executor((source, command, args) -> {
                    resourcePackManager.loadPack();
                    source.sendMessage("GeyserModelEnginePackGenerator reloaded!");
                })
                .build());
    }

    @Subscribe
    public void onPackLoad(GeyserDefineResourcePacksEvent event) {
        if (!extension.getConfigManager().getConfig().getBoolean("options.resource-pack.auto-load")) return;

        ResourcePack resourcePack = ResourcePack.create(PackCodec.path(resourcePackManager.getGeneratedPackZipPath()));
        event.register(resourcePack);
    }

    private void loadManagers() {
        this.configManager = new ConfigManager();
        this.resourcePackManager = new ResourcePackManager(this);
    }

    public static GeyserModelEnginePackGenerator getExtension() {
        return extension;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }
}
