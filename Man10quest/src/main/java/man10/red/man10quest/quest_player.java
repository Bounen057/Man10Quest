package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class quest_player {
    private Man10Quest pl;
    public quest_player(Man10Quest pl) {
        this.pl = pl;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // *  プレイヤーのポイントを加算 *
    // /mq quest player add <MCID> <questの名前> <数字>
    ////////////////////////////////////////////////////////////////////////////////
    //プレイヤーのポイントを加算
    public void pt_add(String[] args){
        Player p = Bukkit.getServer().getPlayer(args[2]);
        ////////////////////////////////////
        // クエストの名前から配列の位置を特定
        ////////////////////////////////////
        String sql = "SELECT * FROM quest_data;";
        ResultSet quest_data = pl.mysql.query(sql);
        List<String> player_pt_array = new ArrayList<>();
        String[] player_pt={};

        int number =-1;
        try {
            while (quest_data.next()) {
                if(args[3].equalsIgnoreCase(quest_data.getString("name"))){
                    number = quest_data.getInt("number");
                }
            }
        } catch (Exception e) {
        }
        // ERROR CHECK
        if(number==-1){
            p.sendMessage(pl.PluginName+"§c§lERROR:そのクエスト名は存在しません! name:"+args[3]);
            return;
        }
        if(pl.others_method.toInt(args[4],p,"pt")==-1){
            return;
        }
        if(args.length!=5){
            p.sendMessage(pl.PluginName+"§c§lERROR:コマンドの使い方が違います /mq help quest");
            return;
        }

        /////////////////////////////////////////////
        // プレイヤーのポイントを加算
        /////////////////////////////////////////////
        sql = "SELECT * FROM player_data WHERE uuid='"+p.getUniqueId()+"';";
        ResultSet player_data = pl.mysql.query(sql);

        try {
            while (player_data.next()) { quest_data.next();
                player_pt = pl.others_method.SQLToArray(player_data.getString("quest_pt"));
                for(int i=0;i < player_pt.length;i++){
                    player_pt_array.add(player_pt[i]);
                }
                ///////////////////////////
                //  クリア判定
                ///////////////////////////
                sql = "SELECT * FROM quest_data WHERE number='"+number+"';";
                quest_data = pl.mysql.query(sql);quest_data.next();
                int max_amount = quest_data.getInt("pt");

                if(pl.others_method.toInt(player_pt[number],p,"/OPに知らせてください! +"+args[3]+"+/") < max_amount) {
                    player_pt[number] = "" + (Integer.parseInt(player_pt_array.get(number)) + Integer.parseInt(args[4]));

                    if (Integer.parseInt(player_pt[number]) >= max_amount) {
                        player_pt[number] = "" + max_amount;
                        ItemStack[] item = pl.others_method.itemStackArrayFromBase64(quest_data.getString("item"));
                        String item_name = item[0].getItemMeta().getDisplayName();
                        p.sendMessage(pl.PluginName+"§f§l『"+item_name+"§f§l』を§e§lクリア§f§lしました! §a§l/mq");
                    }
                }
            }
        } catch (Exception e) {
        }
        String player_sql = "UPDATE player_data SET quest_pt='"+pl.others_method.ArrayToSQL(player_pt)+"' WHERE uuid='"+p.getUniqueId()+"';";
        pl.mysql.execute(player_sql);
    }



    ////////////////////////////////////////////////////////////////////////////////
    // * クエストを消去とともにクエストを消去 *
    ////////////////////////////////////////////////////////////////////////////////
    public void pt_delete(Player p,String quest_name){
        ////////////////////////////////////
        // クエストの名前から配列の位置を特定
        ////////////////////////////////////
        String sql = "SELECT * FROM quest_data;";
        ResultSet player_data = pl.mysql.query(sql);
        List<String> player_pt_array = new ArrayList<>();
        String[] player_pt={};

        int number =-1;
        try {
            while (player_data.next()) {
                if(quest_name.equalsIgnoreCase(player_data.getString("name"))){
                    number = player_data.getInt("number");
                }
            }
        } catch (Exception e) {
        }
        // ERROR CHECK
        if(number==-1){
            p.sendMessage(pl.PluginName+"§c§lERROR:そのクエスト名は存在しません! "+quest_name);
            return;
        }

        /////////////////////////////////////////////
        // 全プレイヤーのポイントを消去
        /////////////////////////////////////////////
        sql = "SELECT * FROM player_data;";
        player_data = pl.mysql.query(sql);
        String[] player_removedPT;
        try {
            while (player_data.next()) {
                player_pt = pl.others_method.SQLToArray(player_data.getString("quest_pt"));
                player_removedPT = new String[player_pt.length-1];
                for(int i=0;i < player_pt.length;i++){
                    player_pt_array.add(player_pt[i]);
                }
                player_pt_array.remove(number);
                for(int i=0;i < player_removedPT.length;i++){
                    player_removedPT[i]=player_pt_array.get(i);
                }
                String player_sql = "UPDATE player_data SET quest_pt='"+pl.others_method.ArrayToSQL(player_removedPT)+"' WHERE uuid='"+player_data.getString("uuid")+"';";
                pl.mysql.execute(player_sql);
            }
        } catch (Exception e) {
        }

        sql = "DELETE FROM quest_data WHERE name='" + quest_name + "';";
        pl.mysql.execute(sql);

        p.sendMessage(pl.PluginName+"§a§lクエストを消去しました!");
    }
}
