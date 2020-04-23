/*
 * MIT License
 *
 * Copyright (c) 2020 Ursin Filli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.ursinn.spigot.gametemplate.listeners;

import dev.ursinn.spigot.gamelib.enums.GameStatsEnum;
import dev.ursinn.spigot.gamelib.events.GamePlayerDeathEvent;
import dev.ursinn.spigot.gamelib.utils.GameMethodUtils;
import dev.ursinn.spigot.gametemplate.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setDeathMessage(null);
        Bukkit.getPluginManager().callEvent(new GamePlayerDeathEvent(p));
    }

    @EventHandler
    public void onGameDeath(GamePlayerDeathEvent e) {
        Player p = e.getPlayer();
        new GameMethodUtils(Main.getInstance()).respawn(p);
        Player k = p.getKiller();
        Main.getInstance().getStatsAPI().updateStats(p.getUniqueId().toString(), GameStatsEnum.DEATHS, Main.getInstance().deaths.get(p.getUniqueId()) + 1);
        Main.getInstance().deaths.replace(p.getUniqueId(), Main.getInstance().deaths.get(p.getUniqueId()) + 1);
        if (k == null) {
            p.sendMessage("§7[§3Template§7] §7Du §7bist §7gestorben.");
            return;
        }
        p.sendMessage("§7[§3Template§7] §7Du §7wurdest §7von " + k.getName() + " §7getötet.");
        k.sendMessage("§7[§3Template§7] §7Du §7hast " + p.getName() + " §7getötet.");
        Main.getInstance().getCoinsAPI().giveCoins(k.getUniqueId().toString(), 20);
        Main.getInstance().getStatsAPI().updateStats(k.getUniqueId().toString(), GameStatsEnum.KILLS, Main.getInstance().kills.get(k.getUniqueId()) + 1);
        Main.getInstance().kills.replace(k.getUniqueId(), Main.getInstance().kills.get(k.getUniqueId()) + 1);
        k.playSound(k.getLocation(), Sound.LEVEL_UP, 10F, 1F);
    }

}
