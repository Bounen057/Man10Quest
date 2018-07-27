package man10.red.man10quest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.Base64;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class quest_set {
    private Man10Quest pl;
    public quest_set(Man10Quest pl) {
        this.pl = pl;
    }

    public void quest_set(String[] args,Player p){
        ItemStack[] p_item={p.getInventory().getItemInMainHand()};
        Charset charset = StandardCharsets.UTF_8;
        String p_item_base64= pl.others_method.itemStackArrayToBase64(p_item);

        String sql = "SELECT * FROM quest_data;";
        ResultSet quest_data = pl.mysql.query(sql);

        int number=0;
        try {
            while (quest_data.next()){
                number++;
            }
        } catch (Exception e) {
        }

        ////////////////////////////////////////////////////////
        //  category 存在するかチェック
        ////////////////////////////////////////////////////////
        boolean isThere = true;
        sql = "SELECT * FROM category_data;";
        ResultSet category_data = pl.mysql.query(sql);
        try {
            while (category_data.next()) {
                if(args[2].equalsIgnoreCase(category_data.getString("name"))){
                    isThere=false;
                }
            }
        } catch (Exception e) {
        }
        if(isThere){
            p.sendMessage(pl.PluginName+"§c§lそのようなカテゴリは存在しません! /mq category list");
            return;
        }

        String event_sql = "INSERT INTO quest_data " +
                "(number,category,name,pt,item) " +
                "VALUES("+number+"" +
                ",'"+args[2]+"'" +
                ",'" + args[3] + "'" +
                "," + Integer.parseInt(args[4]) + "" +
                ",'"+p_item_base64+"');";
        pl.mysql.execute(event_sql);

        ////////////////////////////////////////////
        //   プレイヤーデータ 書き換え
        ////////////////////////////////////////////
        sql = "SELECT * FROM player_data;";
        ResultSet player_data = pl.mysql.query(sql);

        try {
            while (player_data.next()){
                String player_uuid = player_data.getString("uuid");

                String player_pt = player_data.getString("quest_pt");
                if(player_pt.isEmpty() || player_pt==null){
                    String player_sql = "UPDATE player_data SET quest_pt='0' WHERE uuid='"+player_uuid+"';";
                    pl.mysql.execute(player_sql);
                }else {
                    String[] Array_player_pt_copy = pl.others_method.SQLToArray(player_pt);
                    String[] Array_player_pt = new String[Array_player_pt_copy.length + 1];
                    System.arraycopy(
                            Array_player_pt_copy, 0,
                            Array_player_pt, 0,
                            Array_player_pt_copy.length
                    );
                    Array_player_pt[Array_player_pt_copy.length]="0";
                    String player_sql = "UPDATE player_data SET quest_pt='"+pl.others_method.ArrayToSQL(Array_player_pt)+"' WHERE uuid='"+player_uuid+"';";
                    pl.mysql.execute(player_sql);
                }


            }
        } catch (Exception e) {
        }

        p.sendMessage(pl.PluginName+"§a§lクエストをセットしました!");
    }
}
