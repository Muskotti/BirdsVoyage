package fi.tamk.tiko;

public interface MapProperties {
    int tilesAmountWidth    = 20;
    int tilesAmountHeight   = 300;
    int tileWidth           = 64;
    int tileHeight          = 64;
    int mapHeight           = tilesAmountHeight * tileHeight;
    int mapWidth            = tilesAmountWidth  * tileWidth;
    int cameraWidth         = 1280;
    int cameraHeight        = 720;
    String defaultLang      = "eng";
}
