package fi.tamk.tiko;

/**
 * Map properties has all the info of the tiled maps used in the game.
 *
 * @author Toni Vänttinen and Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public interface MapProperties {
    int tilesAmountWidth    = 20;
    int tilesAmountHeight   = 1000;
    int tileWidth           = 64;
    int tileHeight          = 64;
    int mapHeight           = tilesAmountHeight * tileHeight;
    int mapWidth            = tilesAmountWidth  * tileWidth;
    int cameraWidth         = 1280;
    int cameraHeight        = 720;
}
