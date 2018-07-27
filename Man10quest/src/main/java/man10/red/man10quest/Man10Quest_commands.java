package man10.red.man10quest;

import org.bukkit.entity.Player;

import java.sql.ResultSet;

public class Man10Quest_commands {
    private Man10Quest pl;
    public Man10Quest_commands(Man10Quest pl) {
        this.pl = pl;
    }
    //////////////////////////////////////////////////////////
    //   reload / リロード
    //////////////////////////////////////////////////////////
    public void reload(){
        String sql = "SELECT * FROM events WHERE type='place';";
        ResultSet rs = pl.mysql.query(sql);

        pl.event_type.clear();

        pl.event_place_x.clear();
        pl.event_place_y.clear();
        pl.event_place_z.clear();
        pl.event_place_radius.clear();
        pl.event_place_height.clear();
        pl.event_place_world.clear();
        pl.event_place_server.clear();
        pl.event_place_name.clear();
        pl.event_place_command.clear();
        try {
            while (rs.next()) {
                ////////////////////////////////////////////////
                //   変数 MySQLから取得して代入
                ////////////////////////////////////////////////
                pl.event_type.add("place");

                pl.event_place_x.add     (rs.getInt("x"));
                pl.event_place_y.add     (rs.getInt("y"));
                pl.event_place_z.add     (rs.getInt("z"));
                pl.event_place_radius.add(rs.getInt("radius"));
                pl.event_place_height.add(rs.getInt("height"));
                pl.event_place_world.add(rs.getString("world"));
                pl.event_place_server.add(rs.getString("server"));
                pl.event_place_name.add(rs.getString("name"));
                pl.event_place_command.add(rs.getString("command"));
            }
            rs.close();
        } catch (Exception e) {
        }
    }
}
