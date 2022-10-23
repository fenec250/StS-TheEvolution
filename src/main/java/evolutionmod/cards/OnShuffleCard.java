package evolutionmod.cards;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


public interface OnShuffleCard {

	default void onShuffle() { return; }
	default void onShuffleFromDrawPile() { this.onShuffle(); }
	default void onShuffleFromHandPile() { this.onShuffle(); }
	default void onShuffleFromDiscardPile() { this.onShuffle(); }
	default void onShuffleFromExhaustPile() { this.onShuffle(); }
}