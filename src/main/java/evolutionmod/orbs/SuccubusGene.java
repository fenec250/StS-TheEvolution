package evolutionmod.orbs;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.SuccubusGeneAction;
import evolutionmod.cards.AdaptableEvoCard;

public class SuccubusGene extends AbstractGene {
	public static final String ID = "evolutionmod:SuccubusGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public SuccubusGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new SuccubusGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToBottom(new SuccubusGeneAction(p, m, damagePerGene() * times, times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new SuccubusAdaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive and #yEvoke: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + 1 + DESCRIPTION[1] + damagePerGene() + DESCRIPTION[2];
	}

	private static int damagePerGene() {
		int damage = 1;
		if (AbstractDungeon.player.hasPower("evolution:SuccubusForm")) {
			damage += AbstractDungeon.player.getPower("evolution:SuccubusForm").amount;
		}
		return damage;
	}

	private static class SuccubusAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		SuccubusAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			SuccubusGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#F04040]Succubus[]";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new SuccubusAdaptation(this.amount);
		}
	}
}
