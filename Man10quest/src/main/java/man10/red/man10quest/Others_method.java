package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;

public class Others_method {
    private Man10Quest pl;
    public Others_method(Man10Quest pl) {
        this.pl = pl;
    }

    //////////////////////////////////
    // * delete event *
    // SQLの項目を消去
    //////////////////////////////////
    public void option_delete(ItemStack item, Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, pl.PluginName + "§c§l設定を消去してもよろしいですか?");//invの設定
        //////////////////////////////////
        //  アイテム 設置
        ItemStack Confirmation_item;
        ItemMeta Confirmation_itemMeta;
        for (int i = 0; i < 9; i++) {
            // cancel
            if (i <= 4) {
                Confirmation_item = new ItemStack(Material.STAINED_GLASS_PANE);//*
                Confirmation_itemMeta = Confirmation_item.getItemMeta();//*

                Confirmation_item.setDurability((short) 14);
                Confirmation_itemMeta.setDisplayName("§c§lcancel");
                // OK
            } else {
                Confirmation_item = new ItemStack(Material.STAINED_GLASS_PANE);//*
                Confirmation_itemMeta = Confirmation_item.getItemMeta();//*

                Confirmation_item.setDurability((short) 5);
                Confirmation_itemMeta.setDisplayName("§b§lOK");
            }
            Confirmation_item.setItemMeta(Confirmation_itemMeta);
            inv.setItem(i, Confirmation_item);
        }
        // アイテムの設置
        inv.setItem(4, item);
        p.openInventory(inv);
    }

    public void mySQL_delete(Player p,int slot_number,String sql) {
        if(slot_number >=5) {
            pl.mysql.execute(sql);
            p.sendMessage(pl.PluginName + "§a§l消去しました。");
        }
    }

    //////////////////////////////////////////
    //  Bounen057's method 配列->sql
    //////////////////////////////////////////
    public String ArrayToSQL(String[] array_text){
        String str_text = Arrays.toString(array_text);
        str_text = str_text.replace("[","");
        str_text = str_text.replace("]","");
        str_text = str_text.replace(" ","");

        return str_text;
    }

    //////////////////////////////////////////
    //  Bounen057's method sql->配列
    //////////////////////////////////////////
    public String[] SQLToArray(String str_text){
        String[] array_text = str_text.split(",",-1);

        return array_text;
    }

    //////////////////////////////////////////
    //  初回起動時 テンプレートを作る
    /////////////////////////////////////////
    public void base_template(){
        String sql = "SELECT * FROM player_data;";
        ResultSet player_data = pl.mysql.query(sql);
        boolean istemplate = true;
        try {
            while (player_data.next()) {
                if(player_data.getString("uuid").equalsIgnoreCase("template")){
                    istemplate = false;
                }
            }
        }catch (Exception e){
        }
        if(istemplate){
            String sql_template = "INSERT INTO player_data " +
                    "(uuid,mcid,quest_pt,quest_reward,finish_date) " +
                    "VALUES(" +
                    "'template'," +
                    "'template'," +
                    "''," +
                    "''," +
                    "''" +
                    ");";
            pl.mysql.execute(sql_template);
        }
    }

    //////////////////////////////////////////////////////////
    //   toInt / String型からint型へ エラーの場合-1を吐く
    //////////////////////////////////////////////////////////
    public int toInt(String str, Player p, String massege){
        int number=0;
        try {
            number = Integer.parseInt(str);
        }catch (Exception e){
            p.sendMessage(pl.PluginName+"§c§lERROR:"+massege+"は数字を選択してください");
            return -1;
        }
        return number;
    }



    ///////////////////////////////////////////////////
    //  外部メソッド
    //////////////////////////////////////////////////////
    //Mr Takatronix's
    //String(Base64) -> ItemStack
    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    //ItemStack -> String(Base64)
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
}
