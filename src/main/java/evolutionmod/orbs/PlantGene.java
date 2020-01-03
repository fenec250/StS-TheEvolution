package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.ThornsPower;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.LoseThornsPower;

public class PlantGene extends AbstractGene {
	public static final String ID = "evolutionmod:PlantGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public PlantGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new PlantGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int thorns = thornsPerGene() * times;
		int block = blockPerGene() * times;
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(p, p, new ThornsPower(p, thorns), thorns));
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(p, p, new LoseThornsPower(p, thorns), thorns));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, block));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new PlantAdaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
//		this.description = "#yPassive: " + buildDescription() + " NL #yEvoke: ";
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + thornsPerGene() + DESCRIPTION[1] + blockPerGene() + DESCRIPTION[2];
	}

	private static int thornsPerGene() {
		int thorns = 1;
		if (AbstractDungeon.player.hasPower("TODO Plant Form")) {
			thorns += AbstractDungeon.player.getPower("TODO Plant Form").amount;
		}
		return thorns;
	}

	private static int blockPerGene() {
		int block = 2;
		if (AbstractDungeon.player.hasPower("TODO Plant Form")) {
			block += AbstractDungeon.player.getPower("TODO Plant Form").amount;
		}
		return block;
	}

	private static class PlantAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		PlantAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			PlantGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Plant";
		}

		@Override
		public String getGeneId() {
			return ID;
		}
	}
}
