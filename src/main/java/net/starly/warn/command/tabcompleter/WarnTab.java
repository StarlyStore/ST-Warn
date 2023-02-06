package net.starly.warn.command.tabcompleter;

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

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (player.hasPermission("starly.warn.give")) list.add("지급"); if (player.hasPermission("starly.warn.action")) list.add("액션");
            if (player.hasPermission("starly.warn.take")) list.add("차감"); if (player.hasPermission("starly.warn.set")) list.add("설정");
            if (player.hasPermission("starly.warn.check")) list.add("확인");
            return list;
        }

        if (args.length == 2) {
            if (args[0].equals("액션")) {
                if (player.hasPermission("starly.warn.action")) return Arrays.asList("1", "2", "3", "4", "5");
            }
        }

        if (args.length == 3) {
            if (args[0].equals("액션")) {
                if (player.hasPermission("starly.warn.action")) return Arrays.asList("MESSAGE", "KICK", "BAN", "BAN-IP");

            } else if (args[0].equals("지급") || args[0].equals("차감") || args[0].equals("설정")) {
                return Collections.singletonList("<경고 횟수>");

            }else {
                return Collections.emptyList();
            }
        }

        if (args.length == 4) {
            if (args[0].equals("지급") || args[0].equals("차감") || args[0].equals("설정")) {
                return Collections.singletonList("<사유>");
            }
        }

        return null;
    }
}
