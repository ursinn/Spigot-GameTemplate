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

package dev.ursinn.spigot.gametemplate;

import com.google.common.reflect.ClassPath;
import dev.ursinn.spigot.gamelib.GameMap;
import dev.ursinn.spigot.gamelib.apis.GameCoinsAPI;
import dev.ursinn.spigot.gamelib.apis.GameServerAPI;
import dev.ursinn.spigot.gamelib.apis.GameStatsAPI;
import dev.ursinn.spigot.gamelib.enums.GameStatsEnum;
import dev.ursinn.spigot.gamelib.enums.GameStatusEnum;
import dev.ursinn.spigot.gamelib.helpers.GameMapHelper;
import dev.ursinn.spigot.gamelib.helpers.GamePlayerHelper;
import dev.ursinn.spigot.gamelib.helpers.GameUpdateCheckHelper;
import dev.ursinn.spigot.gametemplate.utils.Options;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    public boolean devBuild = true;

    public GameUpdateCheckHelper updateChecker = new GameUpdateCheckHelper(0, this);

    public ArrayList<UUID> players = new ArrayList<>();
    public HashMap<UUID, GamePlayerHelper> playerHelper = new HashMap<>();
    public HashMap<UUID, Integer> kills = new HashMap<>();
    public HashMap<UUID, Integer> deaths = new HashMap<>();

    private Options options;
    private GameStatsAPI statsAPI;
    private GameServerAPI serverAPI;
    private GameCoinsAPI coinsAPI;
    private GameMap map;
    private GameStatusEnum status;
    private GameMapHelper mapHelper;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        options = new Options(this);

        mapHelper = new GameMapHelper(this);

        if (getOptions().isSetup()) {
            setStatus(GameStatusEnum.SETUP);
            return;
        }

        setStatus(GameStatusEnum.IN_GAME);
        setMap(getMapHelper().getRandomMap());

        statsAPI = new GameStatsAPI(getOptions().getGame(), getOptions().getDatabasePrefix(), getOptions().getDatabase());
        ArrayList<GameStatsEnum> stats = new ArrayList<>();
        stats.add(GameStatsEnum.KILLS);
        stats.add(GameStatsEnum.DEATHS);
        getStatsAPI().init(stats);

        coinsAPI = new GameCoinsAPI(getOptions().getDatabasePrefix(), getOptions().getDatabase(), getOptions().getGameCoinsEnum());
        getCoinsAPI().init();

        serverAPI = new GameServerAPI(getOptions().getDatabasePrefix(), getOptions().getDatabase());
        getServerAPI().init(getOptions().getServerId(), getOptions().getGame());
        getServerAPI().updateMap(getMap().getName());
        getServerAPI().updateStatus(getStatus());
        getServerAPI().updateOnline(players.size());
        getServerAPI().updateMax(getMap().getMaxPlayers());

        registerListener();

        if (!devBuild) {
            if (getOptions().useMetrics()) {
                Metrics metrics = new Metrics(this, 7176);
            }

            if (getOptions().useUpdateChecker())
                updateChecker.checkUpdates.start();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Options getOptions() {
        return options;
    }

    public GameServerAPI getServerAPI() {
        return serverAPI;
    }

    public GameStatsAPI getStatsAPI() {
        return statsAPI;
    }

    public GameCoinsAPI getCoinsAPI() {
        return coinsAPI;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public GameStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GameStatusEnum status) {
        this.status = status;
    }

    public GameMapHelper getMapHelper() {
        return mapHelper;
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClassLoader())
                    .getTopLevelClasses("dev.ursinn.spigot.gametemplate.listeners")) {
                Class<Listener> clazz = (Class<Listener>) Class.forName(classInfo.getName());
                if (Listener.class.isAssignableFrom(clazz)) {
                    pluginManager.registerEvents(clazz.getDeclaredConstructor().newInstance(), this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
