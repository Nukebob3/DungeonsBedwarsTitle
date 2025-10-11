package net.nukebob.config.dbtitles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;

public class ConfigScreen extends Screen {
    private final Screen parent;

    DungeonsBedwarsTitlesConfig config = DungeonsBedwarsTitlesConfig.loadConfig();

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

        ButtonWidget toggleVictoryEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.victory").append(" ").append(Text.translatable("db:config." + (config.victory_enabled ? "enabled" : "disabled"))).withColor(config.victory_enabled ? enableColour : disableColour), this::toggleVictoryEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY - 20, buttonWidth, buttonHeight).build();
        ButtonWidget toggleDeathEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.death").append(" ").append(Text.translatable("db:config." + (config.death_enabled ? "enabled" : "disabled"))).withColor(config.death_enabled ? enableColour : disableColour), this::toggleDeathEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY, buttonWidth, buttonHeight).build();
        ButtonWidget toggleBedEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.bed").append(" ").append(Text.translatable("db:config." + (config.bed_enabled ? "enabled" : "disabled"))).withColor(config.bed_enabled ? enableColour : disableColour), this::toggleBedEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY+20, buttonWidth, buttonHeight).build();
        toggleVictoryEnabledWidget.active = config.mod_enabled;
        toggleDeathEnabledWidget.active = config.mod_enabled;
        toggleBedEnabledWidget.active = config.mod_enabled;
        ButtonWidget[] activeableButtons = new ButtonWidget[]{toggleVictoryEnabledWidget, toggleDeathEnabledWidget, toggleBedEnabledWidget};

        ButtonWidget toggleModEnabledWidget = ButtonWidget.builder(Text.translatable("db:config.mod").append(" ").append(Text.translatable("db:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED), button -> toggleModEnabled(button, activeableButtons))
                .dimensions(centerX - buttonWidth / 2, centerY - 45, buttonWidth, buttonHeight).build();

        ButtonWidget doneButtonWidget = ButtonWidget.builder(Text.translatable("db:config.done").withColor(Colors.WHITE), button -> closeScreen())
                .dimensions(centerX - buttonWidth / 2, centerY + 45, buttonWidth - buttonHeight - 5, buttonHeight).build();

        ButtonWidget hypixelOnlyButtonWidget = TextIconButtonWidget.IconOnly.builder(Text.empty(), this::toggleHypixelOnly, true).texture(config.hypixel_only ? Identifier.of(DungeonsBedwars.MOD_ID, "server-icon/hypixel") : Identifier.of(DungeonsBedwars.MOD_ID, "server-icon/minecraft"), 20, 20).build();
        hypixelOnlyButtonWidget.setTooltip(Tooltip.of(config.hypixel_only? Text.translatable("db:config.hypixel"):Text.translatable("db:config.all_servers")));
        hypixelOnlyButtonWidget.setDimensionsAndPosition(20, 20, centerX + buttonWidth / 2 - buttonHeight, centerY + 45);

        this.addDrawableChild(toggleModEnabledWidget);
        this.addDrawableChild(toggleVictoryEnabledWidget);
        this.addDrawableChild(toggleDeathEnabledWidget);
        this.addDrawableChild(toggleBedEnabledWidget);
        this.addDrawableChild(doneButtonWidget);
        this.addDrawableChild(hypixelOnlyButtonWidget);

        super.init();
    }

    private void toggleModEnabled(ButtonWidget buttonWidget, ButtonWidget[] widgets) {
        config.mod_enabled = !config.mod_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.mod").append(" ").append(Text.translatable("db:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED));
        for (ButtonWidget widget : widgets) {
            widget.active = config.mod_enabled;
            if (!config.mod_enabled) {
                widget.setMessage(Text.literal(widget.getMessage().getString()).withColor(Colors.GRAY));
            } else {
                if (widget == widgets[0]) {
                    widget.setMessage(Text.literal(widget.getMessage().getString()).withColor(config.victory_enabled ? enableColour : disableColour));
                } else if (widget == widgets[1]) {
                    widget.setMessage(Text.literal(widget.getMessage().getString()).withColor(config.death_enabled ? enableColour : disableColour));
                }
            }
        }
        DungeonsBedwarsTitlesConfig.saveConfig();
    }

    private void toggleVictoryEnabled(ButtonWidget buttonWidget) {
        config.victory_enabled = !config.victory_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.victory").append(" ").append(Text.translatable("db:config." + (config.victory_enabled ? "enabled" : "disabled"))).withColor(config.victory_enabled ? enableColour : disableColour));
        DungeonsBedwarsTitlesConfig.saveConfig();
    }

    private void toggleDeathEnabled(ButtonWidget buttonWidget) {
        config.death_enabled = !config.death_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.death").append(" ").append(Text.translatable("db:config." + (config.death_enabled ? "enabled" : "disabled"))).withColor(config.death_enabled ? enableColour : disableColour));
        DungeonsBedwarsTitlesConfig.saveConfig();
    }

    private void toggleBedEnabled(ButtonWidget buttonWidget) {
        config.bed_enabled = !config.bed_enabled;
        buttonWidget.setMessage(Text.translatable("db:config.bed").append(" ").append(Text.translatable("db:config." + (config.bed_enabled ? "enabled" : "disabled"))).withColor(config.bed_enabled ? enableColour : disableColour));
        DungeonsBedwarsTitlesConfig.saveConfig();
    }

    private void toggleHypixelOnly(ButtonWidget buttonWidget) {
        config.hypixel_only = !config.hypixel_only;
        this.remove(buttonWidget);
        buttonWidget=TextIconButtonWidget.IconOnly.builder(Text.empty(), this::toggleHypixelOnly, true).texture(config.hypixel_only ? Identifier.of(DungeonsBedwars.MOD_ID, "server-icon/hypixel") : Identifier.of(DungeonsBedwars.MOD_ID, "server-icon/minecraft"), 20, 20).build();
        buttonWidget.setTooltip(Tooltip.of(config.hypixel_only? Text.translatable("db:config.hypixel"):Text.translatable("db:config.all_servers")));

        int buttonWidth = 150;
        int buttonHeight = 20;
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        buttonWidget.setDimensionsAndPosition(20, 20, centerX + buttonWidth / 2 - buttonHeight, centerY + 45);
        this.addDrawableChild(buttonWidget);
        DungeonsBedwarsTitlesConfig.saveConfig();
    }

    private void closeScreen() {
        if (client==null)return;
        this.client.setScreen(this.parent);
    }
}
