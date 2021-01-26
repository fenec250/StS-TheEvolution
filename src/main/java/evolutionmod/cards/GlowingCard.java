package evolutionmod.cards;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


public interface GlowingCard {
	static final String GLOW_INFO_PREFIX = "GlowingCard:";

	default int getNumberOfGlows() { return 1; }
	default boolean isGlowing(int glowIndex) { return true; }

	Color getGlowColor(int glowIndex);

	/**
	 * Use this during receivePostInitialize() to generate a number of
	 * CardBorderGlowManager.GlowInfo to show the glows of cards
	 * implementing GlowingCard.
	 *
	 *
	 * @param maxGlows the number of GlowInfo to create. If a GlowingCard
	 *                 has more glows than there are GlowInfo the
	 *                 exceeding glows will not show.
	 */
	static void init(int maxGlows) {
		for (int i = maxGlows - 1; i >= 0; --i) {
			final int glowIndex = i;
			CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
				@Override
				public boolean test(AbstractCard card) {
					if (!CardCrawlGame.isInARun() || AbstractDungeon.currMapNode == null
							|| AbstractDungeon.getCurrRoom().phase == null
							|| AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
						return false;
					}
					if (card instanceof GlowingCard && ((GlowingCard) card).getNumberOfGlows() > glowIndex) {
						return ((GlowingCard) card).isGlowing(glowIndex);
					}
					return false;
				}

				@Override
				public Color getColor(AbstractCard card) {
					return ((GlowingCard)card).getGlowColor(glowIndex);
				}

				@Override
				public String glowID() {
					return GLOW_INFO_PREFIX + glowIndex;
				}
			});
		}
	}
}