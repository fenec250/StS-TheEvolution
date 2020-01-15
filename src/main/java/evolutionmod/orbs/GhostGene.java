package evolutionmod.orbs;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.GhostGeneAction;
import evolutionmod.actions.SuccubusGeneAction;
import evolutionmod.cards.AdaptableEvoCard;

public class GhostGene extends AbstractGene {
	public static final String ID = "evolutionmod:GhostGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public GhostGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new GhostGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToBottom(new GhostGeneAction(p, m, blockPerGene() * times, times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new GhostAdaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive and #yEvoke: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + 1 + DESCRIPTION[1] + blockPerGene() + DESCRIPTION[2];
	}

	private static int blockPerGene() {
		int damage = 1;
		if (AbstractDungeon.player.hasPower("evolutionmod:GhostForm")) {
			damage += AbstractDungeon.player.getPower("evolutionmod:GhostForm").amount;
		}
		return damage;
	}

	private static class GhostAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		GhostAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			GhostGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Ghost";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new GhostAdaptation(this.amount);
		}
	}
}