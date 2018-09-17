package cf.catworlds.dynamicspigotsetting.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import cf.catworlds.dynamicspigotsetting.MainClass;
import cf.catworlds.dynamicspigotsetting.text.BasicText;
import cf.catworlds.dynamicspigotsetting.text.TextHelper;
import cf.catworlds.dynamicspigotsetting.utils.ServerHelper;

public class MainCommand implements TabExecutor {

	MainClass plugin;

	public MainCommand(MainClass pl) {
		this.plugin = pl;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 2 && args.length != 1)
			return false;
		if (args[0].equalsIgnoreCase(FirstLevel.get(0))) {
			try {
				if (args.length == 1) {
					sender.sendMessage(TextHelper.format(BasicText.ItemMergeCheckCommand, ServerHelper.getItemMerge()));
					return true;
				}
				ServerHelper.setItemMerge(Double.parseDouble(args[1]));
				sender.sendMessage(TextHelper.format(BasicText.ItemMergeCommand, args[1]));
			} catch (Exception e) {
				sender.sendMessage(TextHelper.format(BasicText.CommandNumberError, args[1]));
				e.printStackTrace();
			}
			return true;
		} else if (args[0].equalsIgnoreCase(FirstLevel.get(1))) {
			try {
				if (args.length == 1) {
					sender.sendMessage(TextHelper.format(BasicText.ExpMergeCheckCommand, ServerHelper.getExpMerge()));
					return true;
				}
				ServerHelper.setExpMerge(Double.parseDouble(args[1]));
				sender.sendMessage(TextHelper.format(BasicText.ExpMergeCommand, args[1]));
			} catch (Exception e) {
				sender.sendMessage(TextHelper.format(BasicText.CommandNumberError, args[1]));
				e.printStackTrace();
			}
			return true;
		} else if (args[0].equalsIgnoreCase(FirstLevel.get(2))) {
			plugin.reload();
			sender.sendMessage(TextHelper.format(BasicText.ReloadSuccess));
			return true;
		}
		return false;
	}

	private static final List<String> FirstLevel = Arrays.asList("item", "exp", "reload");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<String>();
		if (args.length == 1)
			StringUtil.copyPartialMatches(args[0], FirstLevel, completions);
		return completions;
	}

}
