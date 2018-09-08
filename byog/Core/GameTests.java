package byog.Core;
import byog.TileEngine.TERenderer;
public class GameTests {
    private static Game rs = new Game();
    private static Game rs2 = new Game();
    private static TERenderer ter = new TERenderer();
    private static TERenderer ter2 = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static void main(String[] args) {
        ter.initialize(WIDTH, HEIGHT, 0, 5);
        ter2.initialize(WIDTH, HEIGHT, 0, 5);
        //ter.renderFrame(rs.playWithInputString("N999SDDDWWWDDD"));
        //System.out.println(rs2.playWithInputString("n999sddd:q")[0][0].description());
        //ter2.renderFrame(rs2.playWithInputString("LWWW:Q"));
        //ter2.renderFrame(rs2.playWithInputString("LDDD:Q"));
        //ter2.renderFrame(rs2.playWithInputString("L:Q"));
        //ter2.renderFrame(rs2.playWithInputString("L:Q"));
        //ter2.renderFrame(rs2.playWithInputString("LWWWDDD"));
        //rs2.playWithInputString("n4979154725301381123swwawd");
        //rs2.playWithInputString("n2950953422994074886sdaddawa");
        //rs2.playWithInputString("n3415218040718096461ssdsddaddaa:q");
        ter2.renderFrame(rs2.playWithInputString("ld"));
    }
}
