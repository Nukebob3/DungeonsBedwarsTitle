package net.nukebob.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class ConfigScreen extends Screen {
    private final Screen parent;

    Config config = ConfigManager.loadConfig();

    int enableColour = 8781731;
    int disableColour = 16745861;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("db:config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 150;
        int buttonHeight = 20;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        ButtonWidget toggleModEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.mod").append(" ").append(Text.translatable("db:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED), this::toggleModEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY - 45, buttonWidth, buttonHeight).build();

        ButtonWidget toggleVictoryEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.victory").append(" ").append(Text.translatable("db:config." + (config.victory_enabled ? "enabled" : "disabled"))).withColor(config.victory_enabled ? enableColour : disableColour), this::toggleVictoryEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY - 20, buttonWidth, buttonHeight).build();
        ButtonWidget toggleDeathEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.death").append(" ").append(Text.translatable("db:config." + (config.death_enabled ? "enabled" : "disabled"))).withColor(config.death_enabled ? enableColour : disableColour), this::toggleDeathEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY, buttonWidth, buttonHeight).build();
        ButtonWidget toggleBedEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.bed").append(" ").append(Text.translatable("db:config." + (config.bed_enabled ? "enabled" : "disabled"))).withColor(config.bed_enabled ? enableColour : disableColour), this::toggleBedEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY + 20, buttonWidth, buttonHeight).build();

        ButtonWidget doneButtonWidget = ButtonWidget.builder(Text.translatable("db:config.done").withColor(Colors.WHITE), button -> closeScreen())
                .dimensions(centerX - buttonWidth / 2, centerY + 45, buttonWidth, buttonHeight).build();

        this.addDrawableChild(toggleModEnabledWidget);
        this.addDrawableChild(toggleVictoryEnabledWidget);
        this.addDrawableChild(toggleDeathEnabledWidget);
        this.addDrawableChild(toggleBedEnabledWidget);
        this.addDrawableChild(doneButtonWidget);

        super.init();
    }

    private void toggleModEnabled(ButtonWidget buttonWidget) {
        config.mod_enabled = !config.mod_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.mod").append(" ").append(Text.translatable("db:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED));
        ConfigManager.saveConfig();
    }

    private void toggleVictoryEnabled(ButtonWidget buttonWidget) {
        config.victory_enabled = !config.victory_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.victory").append(" ").append(Text.translatable("db:config." + (config.victory_enabled ? "enabled" : "disabled"))).withColor(config.victory_enabled ? enableColour : disableColour));
        ConfigManager.saveConfig();
    }

    private void toggleDeathEnabled(ButtonWidget buttonWidget) {
        config.death_enabled = !config.death_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.death").append(" ").append(Text.translatable("db:config." + (config.death_enabled ? "enabled" : "disabled"))).withColor(config.death_enabled ? enableColour : disableColour));
        ConfigManager.saveConfig();
    }

    private void toggleBedEnabled(ButtonWidget buttonWidget) {
        config.bed_enabled = !config.bed_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.bed").append(" ").append(Text.translatable("db:config." + (config.bed_enabled ? "enabled" : "disabled"))).withColor(config.bed_enabled ? enableColour : disableColour));
        ConfigManager.saveConfig();
    }

    private void closeScreen() {
        this.client.setScreen(this.parent);
    }
}
