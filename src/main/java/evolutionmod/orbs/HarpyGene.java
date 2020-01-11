package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;

public class HarpyGene extends AbstractGene {
	public static final String ID = "evolutionmod:HarpyGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public HarpyGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new HarpyGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, times));
		if (p.hasPower("TODO Harpy Form")) {
			int swiftness = p.getPower("TODO Harpy Form").amount;
//			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SwiftnessPower(p, swiftness), swiftness));
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new HarpyAdaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
		String description = DESCRIPTION[0];
		if (AbstractDungeon.player.hasPower("evolution:HarpyForm")) {
			description += DESCRIPTION[1] + AbstractDungeon.player.getPower("evolution:HarpyForm").amount + DESCRIPTION[2];
		}
		return description;
	}

	private static class HarpyAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		HarpyAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			HarpyGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#FFFF80]Harpy[]";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new HarpyAdaptation(this.amount);
		}
	}
}
