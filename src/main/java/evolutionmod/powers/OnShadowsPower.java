package evolutionmod.powers;

public interface OnShadowsPower {
    default void onSelfShadowsTrigger(int shadows, int threshold) {}
//    default void onPlayerShadowsTrigger(int shadows, int threshold) {}
}
