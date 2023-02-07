package net.starly.warn.command.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WarnTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (player.hasPermission("starly.warn.give")) completions.add("지급");
            if (player.hasPermission("starly.warn.action")) completions.add("액션");
            if (player.hasPermission("starly.warn.take")) completions.add("차감");
            if (player.hasPermission("starly.warn.set")) completions.add("설정");
            if (player.hasPermission("starly.warn.check")) completions.add("확인");
        } else if (args.length == 2) {
            if (args[0].equals("액션")) {
                if (player.hasPermission("starly.warn.action"))
                    completions.addAll(Arrays.asList("1", "2", "3", "4", "5"));
            } else if (Arrays.asList("지급", "차감", "설정").contains(args[0])) {
                if (player.hasPermission("starly.warn.give") || player.hasPermission("starly.warn.take") || player.hasPermission("starly.warn.set"))
                    completions = null;
            }
        } else if (args.length == 3) {
            if (args[0].equals("액션")) {
                if (player.hasPermission("starly.warn.action"))
                    completions.addAll(Arrays.asList("MESSAGE", "KICK", "BAN", "BAN-IP"));
            } else if (Arrays.asList("지급", "차감", "설정").contains(args[0])) {
                completions.add("<경고 횟수>");
            }
        } else if (args.length == 4) {
            if (args[0].equals("지급") || args[0].equals("차감") || args[0].equals("설정")) {
                return Collections.singletonList("<사유>");
            }
        }

        return completions;
    }
}
