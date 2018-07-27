package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Event_set  {
    private Man10Quest pl;
    public Event_set(Man10Quest pl) {
        this.pl = pl;
    }
    //////////////////////////////////////////////////////////
    //   eventset / イベントをせっと
    //////////////////////////////////////////////////////////
    public void set_event(Player p, String[] args){
        String event_sql;
        /////////////////////////////////////////////////////
        // /mq event set place <名前> <半径> <高さ> <コマンド>
        if(args[2].equalsIgnoreCase("place")) {
            // 変数
            int radius = pl.others_method.toInt(args[4], p, "<半径>");
            int height = pl.others_method.toInt(args[5], p, "<高さ>");
            String command= "";
            for(int i=6;i < args.length;i++) {
                if(command.isEmpty()) {
                    command = args[i];
                }else{
                    command = command + " " + args[i];
                }
            }
            if (radius != -1 && height != -1) {
                event_sql = "INSERT INTO events " +
                        "(type,name,server,world,x,y,z,radius,height,command) " +
                        "VALUES('place'" +
                        ",'" + args[3] + "'" +
                        ",'" + p.getServer().getServerName()+ "'" +
                        ",'" + p.getWorld().getName() + "'" +
                        "," + p.getLocation().getX() + "" +
                        "," + p.getLocation().getY() + "" +
                        "," + p.getLocation().getZ() + "" +
                        "," + radius + "" +
                        "," + height + "" +
                        ",'" +command + "');";
                pl.mysql.execute(event_sql);
            }else{
                return;
            }
        }
        p.sendMessage(pl.PluginName+"§a§l"+args[3]+"というイベントを作りました!");
        pl.Man10Quest_commands.reload();
    }
}
