package com.rappytv.anticmdchoker;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.rappytv.anticmdchoker.listener.IMessageSendSubscriber;
import com.rappytv.anticmdchoker.listener.MessageSendListener;
import com.rappytv.anticmdchoker.listener.subscriber.CommandChokerSubscriber;
import com.rappytv.anticmdchoker.listener.subscriber.SendAnywaySubscriber;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.concurrent.Executors;

public class AntiCommandChoker extends LabyModAddon {
    private static AntiCommandChoker instance;
    public static String prefix = "\u00A76\u00A7lACC \u00A78\u00bb \u00A7r";
    private ListeningExecutorService asyncListeningExecutor;


    public static boolean enabled = true;
    public static boolean replaceChokeTrigger = false;

    public AntiCommandChoker() {
    }

    public static AntiCommandChoker getInstance() {
        return instance;
    }

    public ListeningExecutorService getAsyncListeningExecutor() {
        return this.asyncListeningExecutor;
    }

    public void onEnable() {
        instance = this;
        this.asyncListeningExecutor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        this.getApi().getEventManager().register(MessageSendListener.create(new IMessageSendSubscriber[]{new SendAnywaySubscriber(), new CommandChokerSubscriber()}));
    }

    public void loadConfig() {
        enabled = getConfig().has("enabled") ? getConfig().get("enabled").getAsBoolean() : enabled;
        replaceChokeTrigger = getConfig().has("replaceChokeTrigger") ? getConfig().get("replaceChokeTrigger").getAsBoolean() : replaceChokeTrigger;
    }

    protected void fillSettings(List<SettingsElement> list) {
        BooleanElement enabledEl = new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {

            @Override
            public void accept(Boolean accepted) {
                enabled = accepted;

                getConfig().addProperty("enabled", enabled);
                saveConfig();
            }
        }, true);

        BooleanElement replaceEl = new BooleanElement("Replace the 7 with a / instead of warning you", new ControlElement.IconData(new ResourceLocation("anticmdchoker/slash.png")), new Consumer<Boolean>() {

            @Override
            public void accept(Boolean accepted) {
                replaceChokeTrigger = accepted;

                getConfig().addProperty("replaceChokeTrigger", replaceChokeTrigger);
                saveConfig();
            }
        }, true);

        list.add(enabledEl);
        list.add(replaceEl);
    }
}
