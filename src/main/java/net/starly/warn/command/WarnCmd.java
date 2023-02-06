package net.starly.warn.command;

import net.starly.warn.WarnMain;
import net.starly.warn.data.WarnData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.starly.warn.WarnMain.config;

public class WarnCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (args.length == 0) {
            if (player.isOp()) config.getMessages("messages.warn.help").forEach(player::sendMessage);
            else Bukkit.dispatchCommand(player, "경고 확인 " + player.getName());
            return true;
        }

        WarnData warnData = WarnMain.getWarnData();

        switch (args[0]) {
            case "도움말": {
                if (!player.hasPermission("starly.warn.help")) {
                    player.sendMessage(config.getMessage("errorMessages.noPermission"));
                    return true;
                }

                config.getMessages("message.help").forEach(player::sendMessage);
                return true;
            }
            case "확인": {
                if (!player.hasPermission("starly.warn.check")) {
                    player.sendMessage(config.getMessage("errorMessages.noPermission"));
                    return true;
                }

                if (args.length == 1) {
                    Bukkit.dispatchCommand(player, "경고 확인 " + player.getName());
                    return true;
                }

                if (args.length != 2) {
                    player.sendMessage(config.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(config.getMessage("errorMessages.warn.notFoundPlayer"));
                    return true;
                }

                player.sendMessage(config.getMessage("messages.warn.check")
                        .replace("{player}", target.getName())
                        .replace("{amount}", String.valueOf(warnData.get(target))));

                return true;
            }
            case "지급":
            case "차감":
            case "설정": {
                if (args.length == 1) {
                    player.sendMessage(config.getMessage("errorMessages.warn.noPlayer"));
                    return true;
                }

                if (args.length == 2) {
                    player.sendMessage(config.getMessage("errorMessages.warn.noAmount"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(config.getMessage("errorMessages.warn.notFoundPlayer"));
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    player.sendMessage(config.getMessage("errorMessages.warn.invalidAmount"));
                    return true;
                }

                boolean isPublic = config.getBoolean("others.public");

                String reason;
                if (args.length > 3) reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                else reason = null;

                switch (args[0]) {
                    case "지급": {
                        if (!player.hasPermission("starly.warn.give")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.give")
                                .stream()
                                .map(s -> s.replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount)).replace("{reason}", reason == null ? "없음" : reason))
                                .collect(Collectors.toList());

                        if (isPublic) msg.forEach(Bukkit::broadcastMessage);
                        else {
                            msg.forEach(player::sendMessage);
                            msg.forEach(target::sendMessage);
                        }
                        warnData.give(target, amount);
                        return true;
                    }
                    case "차감": {
                        if (!player.hasPermission("starly.warn.take")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.take")
                                .stream()
                                .map(s -> s.replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount)).replace("{reason}", reason == null ? "없음" : reason))
                                .collect(Collectors.toList());

                        if (isPublic) msg.forEach(Bukkit::broadcastMessage);
                        else {
                            msg.forEach(player::sendMessage);
                            msg.forEach(target::sendMessage);
                        }
                        warnData.take(target, amount);
                        return true;
                    }
                    case "설정": {
                        if (!player.hasPermission("starly.warn.set")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.set")
                                .stream()
                                .map(s -> s.replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount)).replace("{reason}", reason == null ? "없음" : reason))
                                .collect(Collectors.toList());

                        if (isPublic) msg.forEach(Bukkit::broadcastMessage);
                        else {
                            msg.forEach(player::sendMessage);
                            msg.forEach(target::sendMessage);
                        }
                        warnData.set(target, amount);
                        return true;
                    }
                }
            }
            case "액션": {
                if (!player.hasPermission("starly.warn.action")) {
                    player.sendMessage(config.getMessage("errorMessages.noPermission"));
                    return true;
                }

                if (args.length == 1) {
                    player.sendMessage(config.getMessage("errorMessages.warn.noActionLine"));
                    return true;
                }

                if (args.length == 2) {
                    player.sendMessage(config.getMessage("errorMessages.warn.noAction"));
                    return true;
                }

                if (args.length != 3) {
                    player.sendMessage(config.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                if (!(args[2].equalsIgnoreCase("kick") || args[2].equalsIgnoreCase("ban") || args[2].equalsIgnoreCase("message") || args[2].equalsIgnoreCase("ban-ip"))) {
                    player.sendMessage(config.getMessage("errorMessages.warn.invalidAction"));
                    return true;
                }

                int actionLine;
                try {
                    actionLine = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(config.getMessage("errorMessages.warn.invalidAmount"));
                    return true;
                }
                String action = args[2];
                config.setString("others.actions." + actionLine, action);
                player.sendMessage(config.getMessage("messages.warn.setAction")
                        .replace("{line}", String.valueOf(actionLine))
                        .replace("{action}", action));
                return true;
            }
        }

        return true;
    }
}
