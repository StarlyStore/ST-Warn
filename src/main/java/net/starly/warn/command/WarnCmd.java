package net.starly.warn.command;

import net.starly.warn.data.PlayerWarnData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
                if (args.length == 1) {
                    if (!player.hasPermission("starly.warn.check.other")) {
                        player.sendMessage(config.getMessage("errorMessages.noPermission"));
                        return true;
                    }
                    //

                    player.sendMessage(config.getMessage("messages.warn.check").replace("{player}", player.getDisplayName()).replace("{amount}", new PlayerWarnData(player).getWarn() + ""));
                } else if (args.length == 2) {
                    if (!player.hasPermission("starly.warn.check.self")) {
                        player.sendMessage(config.getMessage("errorMessages.noPermission"));
                        return true;
                    }


                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (!(target.hasPlayedBefore() || target.isOnline())) {
                        player.sendMessage(config.getMessage("errorMessages.warn.notFoundPlayer"));
                        return true;
                    }


                    player.sendMessage(config.getMessage("messages.warn.check").replace("{player}", target.getName()).replace("{amount}", new PlayerWarnData(target).getWarn() + ""));
                } else {
                    player.sendMessage(config.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                return true;
            }

            case "추가":
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


                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (!(target.hasPlayedBefore() || target.isOnline())) {
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
                    case "추가": {
                        if (!player.hasPermission("starly.warn.add")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.add").stream().map(s -> s.replace("{player}", target.getName()).replace("{amount}", amount + "").replace("{reason}", reason == null ? "없음" : reason)).collect(Collectors.toList());
                        if (isPublic) {
                            msg.forEach(Bukkit::broadcastMessage);
                        } else {
                            msg.forEach(player::sendMessage);
                            if (target.isOnline()) msg.forEach(line -> target.getPlayer().sendMessage(line));
                        }


                        new PlayerWarnData(target).addWarn(amount);
                        return true;
                    }

                    case "차감": {
                        if (!player.hasPermission("starly.warn.take")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        PlayerWarnData warnData = new PlayerWarnData(target);
                        if (warnData.getWarn() - amount < 0) {
                            player.sendMessage(config.getMessage("errorMessages.warn.underFlow"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.take").stream().map(s -> s.replace("{player}", target.getName()).replace("{amount}", amount + "").replace("{reason}", reason == null ? "없음" : reason)).collect(Collectors.toList());
                        if (isPublic) msg.forEach(Bukkit::broadcastMessage);
                        else {
                            msg.forEach(player::sendMessage);
                            if (target.isOnline()) msg.forEach(line -> target.getPlayer().sendMessage(line));
                        }


                        warnData.removeWarn(amount);
                        return true;
                    }

                    case "설정": {
                        if (!player.hasPermission("starly.warn.set")) {
                            player.sendMessage(config.getMessage("errorMessages.noPermission"));
                            return true;
                        }

                        if (amount < 0) {
                            player.sendMessage(config.getMessage("errorMessages.warn.underFlow"));
                            return true;
                        }

                        List<String> msg = config.getMessages("messages.warn.set").stream().map(s -> s.replace("{player}", target.getName()).replace("{amount}", amount + "").replace("{reason}", reason == null ? "없음" : reason)).collect(Collectors.toList());
                        if (isPublic) msg.forEach(Bukkit::broadcastMessage);
                        else {
                            msg.forEach(player::sendMessage);
                            if (target.isOnline()) msg.forEach(line -> target.getPlayer().sendMessage(line));
                        }


                        new PlayerWarnData(target).setWarn(amount);
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

                if (!Arrays.asList("kick", "ban", "message", "ban-ip").contains(args[2].toLowerCase())) {
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


                player.sendMessage(config.getMessage("messages.warn.setAction").replace("{line}", String.valueOf(actionLine)).replace("{action}", action));
                return true;
            }
        }

        return true;
    }
}
