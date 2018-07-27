package man10.red.man10quest;

import org.bukkit.entity.Player;

public class helps {
    private Man10Quest pl;
    public helps(Man10Quest pl) {
        this.pl = pl;
    }
    //////////////////////////////////////////////////////////
    //   help / ヘルプ
    //////////////////////////////////////////////////////////
    public void help(Player p){
        p.sendMessage("§e§l=="+pl.PluginName+"§e§l======================================");
        p.sendMessage("§a§l/mq §3§l-クエストの画面を開きます");
        p.sendMessage("§6§l/mq help event §f§l:eventのhelpを開きます");
        p.sendMessage("§6§l/mq help quest §f§l:questのhelpを開きます");
        p.sendMessage("§6§l/mq help category §f§l:categoryのhelpを開きます");
        p.sendMessage("§e§l==================================================");
    }

    //////////////////////////////////////////////////////////
    //   event help / イベントのヘルプ
    //////////////////////////////////////////////////////////
    public void event(Player p){
        p.sendMessage("§e§l=="+pl.PluginName+"§5§l Events §e§l==========================");
        p.sendMessage("§a§l/mq event...");
        p.sendMessage("§6§lset place <name> <半径> <高さ> <コマンド> §f§l:場所イベント");
        p.sendMessage("§6§loption §f§l:設定画面を開きます");
        p.sendMessage("");
        p.sendMessage("§f§l<コマンド> - {player} 対象者のMCIDに置換されます");
        p.sendMessage("§e§l==================================================");
    }

    //////////////////////////////////////////////////////////
    //   quest help / クエストのヘルプ
    //////////////////////////////////////////////////////////
    public void quest(Player p) {
        p.sendMessage("§e§l==" + pl.PluginName + "§b§l Quests §e§l=======================");
        p.sendMessage("§a§l/mq quest...");
        p.sendMessage("§6§lset <category> <name> <pt> §f§l:新しいクエストを作ります");
        p.sendMessage("§6§lplayer <MCID> <クエストの名前> <pt> §f§l:プレイヤーのポイントを加算");
        p.sendMessage("");
        p.sendMessage("§f§lクエストの名前は/mq quest optionで確認可能です");
        p.sendMessage("§e§l==================================================");
    }

    //////////////////////////////////////////////////////////
    //   category help / カテゴリーのヘルプ
    //////////////////////////////////////////////////////////
    public void category(Player p) {
        p.sendMessage("§e§l==" + pl.PluginName + "§a§l Quest §e§l=======================");
        p.sendMessage("§a§l/mq category...");
        p.sendMessage("§6§lset <管理名> §f§l:アイテムを手に持ってください。クエストを作成します");
        p.sendMessage("§6§loption §f§l:カテゴリーの設定画面を開きます");
        p.sendMessage("§6§llist §f§l:カテゴリ一覧の表示");
        p.sendMessage("§e§l===================================================");
    }
}
