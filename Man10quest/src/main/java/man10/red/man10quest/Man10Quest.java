package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Timer;

public class Man10Quest extends JavaPlugin implements Listener {
    ////////////////////////////////
    //      コンストラクタ
    ////////////////////////////////
        Others_method others_method = new Others_method(this);
        //Event_check event = new Event_check(this);
        Man10Quest_commands Man10Quest_commands = new Man10Quest_commands(this);
        Event_option option = new Event_option(this);
        quest_open quest = new quest_open(this);
        Event_set setevent = new Event_set(this);
        quest_set quest_set = new quest_set(this);
        quest_category quest_category = new quest_category(this);
        MySQLManager mysql = new MySQLManager(this, "MQ");
        quest_option quest_option = new quest_option(this);
        quest_player quest_player = new quest_player(this);
        GUIClickEvents guiClickEvents = new GUIClickEvents(this);
        helps help = new helps(this);

    public FileConfiguration config1;
    ////////////////////////////////
    //   変数
    ////////////////////////////////
    BukkitTask task = null;//タイマー用
    public String PluginName = "§a§l[§d§lMa§f§ln§a§l10§2§lQuest§a§l]";//プラグインの名前 チャットで使われる
    //  Event - 比較用変数
    public List<String> event_type = new ArrayList<>();//イベントの種類

    public List<Integer> event_place_x = new ArrayList<>();//場所比較用 event_place_x
    public List<Integer> event_place_y = new ArrayList<>();//場所比較用 event_place_y
    public List<Integer> event_place_z = new ArrayList<>();//場所比較用 event_place_z
    public List<Integer> event_place_radius = new ArrayList<>();//場所比較用 event_place_radius 半径
    public List<Integer> event_place_height = new ArrayList<>();//場所比較用 event_place_height 高さ
    public List<String> event_place_server = new ArrayList<>();//場所比較用 event_place_server 名前
    public List<String> event_place_world = new ArrayList<>();//場所比較用 event_place_world 名前
    public List<String> event_place_name = new ArrayList<>();//場所比較用 event_place_name 名前
    public List<String> event_place_command = new ArrayList<>();//場所比較用 event_place_command コマンド

    public HashMap<UUID,String> questOption_selecting = new HashMap<>();
    public HashMap<UUID,String> questReward_selecting = new HashMap<>();

    public String delete_sql;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        config1 = config;

        getCommand("mq").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);
        ////////////////////
        // MySQL tableの作成
        String sql="CREATE TABLE IF NOT EXISTS events\n" +
                " (type text,name text,server text,world text,x int,y int,z int,radius int,height int,command text);";
        mysql.execute(sql);
        sql="CREATE TABLE IF NOT EXISTS player_data\n" +
                " (uuid text,mcid text,quest_pt text,quest_reward text,finish_date text);";//quest_name int,quest_reward int
        mysql.execute(sql);
        sql="CREATE TABLE IF NOT EXISTS quest_data\n" +
                "  (number int,category text,name text,pt int,reward text,item text);";
        mysql.execute(sql);
        sql="CREATE TABLE IF NOT EXISTS category_data" +
                " (name text,item text);";
        mysql.execute(sql);


        others_method.base_template();

        task = this.getServer().getScheduler().runTaskTimer(this, new Event_check(this ,5), 0L, 20L);

        Man10Quest_commands.reload();
    }

    ////////////////////////////////////////////
    //   Commands - コマンド
    ////////////////////////////////////////////
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            quest_category.select_category((Player) sender);
            return false;
        }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    help.help((Player) sender);
                    return false;
                }
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("event")) {
                        help.event((Player) sender);
                    }
                    if (args[1].equalsIgnoreCase("quest")) {
                        help.quest((Player) sender);
                    }
                    if (args[1].equalsIgnoreCase("category")) {
                        help.category((Player) sender);
                    }
                    return false;
                }
                ((Player) sender).sendMessage(PluginName+"§c§lERROR:コマンドの使い方が間違っています! /mq help");
                return false;
            }

        ////////////////////////////////
        //   events - イベント関連
        ////////////////////////////////
        if(args[0].equalsIgnoreCase("event")) {
            if (args[1].equalsIgnoreCase("set")) {
                setevent.set_event((Player) sender, args);
                return false;
            }

            if(args.length==2) {
                if (args[1].equalsIgnoreCase("option")) {
                    option.open_optionEvent((Player) sender, 0);
                }
                if (args[1].equalsIgnoreCase("help")) {
                   help.event((Player) sender);
                }
                return false;
            }
            ((Player) sender).sendMessage(PluginName+"§c§lERROR:コマンドの使い方が間違っています! /mq help event");
            return false;
        }


        ////////////////////////////////
        //   quests - クエスト関連
        ////////////////////////////////
        if(args[0].equalsIgnoreCase("quest")) {
            if(args.length==5) {
                if (args[1].equalsIgnoreCase("set")) {
                    quest_set.quest_set(args, (Player) sender);
                    quest_option.select_item((Player) sender, args[3]);
                }
                if (args[1].equalsIgnoreCase("player")) {
                        quest_player.pt_add(args);
                }
                return false;
            }
            if(args.length==2) {
                if (args[1].equalsIgnoreCase("option")) {
                    quest_option.quest_option_category((Player) sender);
                }
                if (args[1].equalsIgnoreCase("help")) {
                    help.quest((Player) sender);
                }
                return false;
            }
            ((Player) sender).sendMessage(PluginName+"§c§lERROR:コマンドの使い方が間違っています! /mq help quest");
            return false;
        }

        ////////////////////////////////
        //   category - カテゴリ関連
        ////////////////////////////////
        if(args[0].equalsIgnoreCase("category")) {

            if(args.length==3) {
                if (args[1].equalsIgnoreCase("set")) {
                    quest_category.category_set(args[2], ((Player) sender).getInventory().getItemInMainHand(), (Player) sender);
                }
                return false;
            }
            if(args.length==2) {
                if (args[1].equalsIgnoreCase("help")) {
                    help.category((Player) sender);
                }
                if (args[1].equalsIgnoreCase("list")) {
                    quest_category.category_list((Player) sender);
                }
                if (args[1].equalsIgnoreCase("option")) {
                    quest_category.category_option((Player) sender);
                }
                return false;
            }
        }
        ((Player) sender).sendMessage(PluginName+"§c§lERROR:コマンドの使い方が間違っています! /mq help category");
        return false;
    }

    ////////////////////////////////////////////
    //   Events - イベント
    ////////////////////////////////////////////
    @EventHandler

    public void onMove(PlayerMoveEvent e){
        //event.onMoveEvent(e.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        guiClickEvents.click_event(e);
    }
}
