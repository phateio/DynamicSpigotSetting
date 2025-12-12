package io.github.phateio.dynamicspigotsetting.text;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextHelper {
    static private Map<BasicText, TextFormater> text_map = new HashMap<>();

    static public String format(BasicText text, Object... args) {
        return text_map.get(text).format(args);
    }

    static public void init(FileConfiguration config) {
        text_map.clear();
        for (BasicText text : BasicText.values()) {
            final String textPATH = text.name();
            if (!config.isList(textPATH))
                config.set(textPATH, text.defaultTexts);
            List<String> read = config.getStringList(textPATH);
            read.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
            TextFormater formater = new TextFormater(read, text.formatKeys);
            text_map.put(text, formater);
        }
    }

}
