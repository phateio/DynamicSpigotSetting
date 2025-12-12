package io.github.phateio.dynamicspigotsetting.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BasicText {
    ReloadSuccess("&a重新讀取完成!"),

    TpsNow(new String[]{"TPS"}, "目前的 TPS 為 ${TPS}"),

    NoPermission("&cYou don't have permission."),

    NotPlayer("&cYou must be a player."),
    /**
     * "Path", "ConfigValue", "ValueType", "DefaultValue"
     **/
    ConfigLoadError(new String[]{"Path", "ConfigValue", "ValueType", "DefaultValue"}, "&cConfig load fail [&e${Path} &c= &e${ConfigValue,Null}&c] : Cannot cast to type &b${ValueType}&c, use default &e${DefaultValue,Null}"),

    ItemMergeCommand(new String[]{"Radius"}, "&a成功調整 物品合併半徑為 &e&l${Radius}"),

    ItemMergeCheckCommand(new String[]{"Radius"}, "&a目前的物品合併半徑為 &e&l${Radius}"),

    ExpMergeCommand(new String[]{"Radius"}, "&a成功調整 經驗球合併半徑為 &e&l${Radius}"),

    ExpMergeCheckCommand(new String[]{"Radius"}, "&a目前的 經驗球合併半徑為 &e&l${Radius}"),

    CommandNumberError(new String[]{"Number"}, "&c指令執行失敗。 &e${Number} &c不是一個數字!");

    final List<String> defaultTexts;
    final List<String> formatKeys;

    BasicText(String... def) {
        this(new String[0], def);
    }

    BasicText(String[] keys, String... def) {
        this.defaultTexts = Collections.unmodifiableList(Arrays.asList(def));
        this.formatKeys = Collections.unmodifiableList(Arrays.asList(keys));
    }
}
