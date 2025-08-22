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
import re.imc.geysermodelenginepackgenerator.generator.Entity;
import re.imc.geysermodelenginepackgenerator.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;

public class GeyserModelEnginePackGenerator implements Extension {

    private static GeyserModelEnginePackGenerator extension;

    private File source;

    private Path generatedPackZip;

    private ConfigManager configManager;

    @Subscribe
    public void onLoad(GeyserPreInitializeEvent event) {
        extension = this;

        loadManagers();

        source = dataFolder().resolve("input").toFile();
        source.mkdirs();
        loadConfig();
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
                    loadConfig();
                    source.sendMessage("GeyserModelEnginePackGenerator reloaded!");
                })
                .build());
    }

    public void loadConfig() {
        File generatedPack = dataFolder().resolve("generated_pack").toFile();

        GeneratorMain.startGenerate(source, generatedPack);

        generatedPackZip = dataFolder().resolve("generated_pack.zip");

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(generatedPackZip))) {
            ZipUtil.compressFolder(generatedPack, null, zipOutputStream);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }

        for (Entity entity : GeneratorMain.entityMap.values()) {
            entity.register();
        }
    }

    private void loadManagers() {
        this.configManager = new ConfigManager();
    }

    @Subscribe
    public void onPackLoad(GeyserDefineResourcePacksEvent event) {
        if (!configManager.getConfig().getBoolean("options.resource-pack.auto-load")) return;

        ResourcePack resourcePack = ResourcePack.builder(PackCodec.path(generatedPackZip)).build();
        event.register(resourcePack);
    }

    public static GeyserModelEnginePackGenerator getExtension() {
        return extension;
    }
}
