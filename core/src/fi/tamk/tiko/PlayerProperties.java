package fi.tamk.tiko;

/**
 * Player properties has the information of players speeds, texture size and starting position on the screen.
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public interface PlayerProperties {
    float normalSpeed = 2f;
    float halfSpeed = 1f;
    float playerWidth = 128;
    float playerHeight = 64;
    float moveSpeedXog = 200f;
    float moveSpeedYog = 200f;
    int startX = 640;
    int startY = 300;
}
